//package docker.websocket.exec;
//
//
//import docker.websocket.exec.ws.ContainerExecHandshakeInterceptor;
//import docker.websocket.exec.ws.ContainerExecWSHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
///**
// * 加载websocket.
// * @author lurenjia
// */
//@Configuration
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    //在logs中存在,所以在此处注释
////    @Bean
////    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
////        return new ServerEndpointExporter();
////    }
//
//    @Bean
//    public ContainerExecWSHandler containerExecWSHandler(){
//        return new ContainerExecWSHandler();
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(containerExecWSHandler(), "/ws/container/exec").addInterceptors(new ContainerExecHandshakeInterceptor()).setAllowedOrigins("*");
//    }
//}