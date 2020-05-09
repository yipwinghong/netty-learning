package com.ywh.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywh.http.pojo.Response;
import com.ywh.http.serializer.JSONSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author ywh
 * @since 2020/3/24/024
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpHeaders headers;
    private FullHttpRequest fullRequest;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    private static final Logger log = LoggerFactory.getLogger(HttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        Response response = new Response();
        response.setData("ywh");
        response.setCode(200);
        response.setTimestamp(new Date());

        if (!(msg instanceof HttpRequest )) {
            return;
        }

        HttpRequest request = (HttpRequest) msg;
        headers = request.headers();
        String uri = request.uri();
        log.info("http uri: " + uri);
        if (uri.equals(FAVICON_ICO)) {
            return;
        }
        HttpMethod method = request.method();
        if (method.equals(HttpMethod.GET)) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
            // 业务逻辑
            processAttributes(queryDecoder.parameters());
            response.setMethod("get");
        } else if (method.equals(HttpMethod.POST)) {
            //POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
            fullRequest = (FullHttpRequest) msg;
            //根据不同的Content_Type处理body数据
            processContentType();
            response.setMethod("post");

        }

        JSONSerializer jsonSerializer = new JSONSerializer();
        byte[] content = jsonSerializer.serialize(response);

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
        fullHttpResponse.headers().set(CONTENT_TYPE, "application/json");
        fullHttpResponse.headers().setInt(CONTENT_LENGTH, fullHttpResponse.content().readableBytes());

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (!keepAlive) {
            ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        } else {
            fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(fullHttpResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 简单处理常用几种 Content-Type 的 POST 内容（可自行扩展）
     *
     */
    private void processContentType() {

        String typeStr = headers.get("Content-Type");
        String contentType = typeStr.split(";")[0];

        // 可以使用 HttpJsonDecoder
        if ("application/json".equals(contentType)) {
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            JSONObject obj = JSON.parseObject(jsonStr);
            for (Map.Entry<String, Object> item : obj.entrySet()) {
                log.info(item.getKey() + "=" + item.getValue().toString());
            }

        } else if ("application/x-www-form-urlencoded".equals(contentType)) {
            //方式一：使用 QueryStringDecoder
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
            processAttributes(queryDecoder.parameters());

        } else if ("multipart/form-data".equals(contentType)) {
            //TODO 用于文件上传
        } else {
            //do nothing...
        }
    }

    private void processAttributes(Map<String, List<String>> uriAttributes) {
        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            for (String attrVal : attr.getValue()) {
                log.info(attr.getKey() + "=" + attrVal);
            }
        }
    }

}
