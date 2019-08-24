package controller;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用@CrossOrigin代替
 */
public class SimpleCORSFilter implements Filter {

	@SuppressWarnings("unused")
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String origin = (String) servletRequest.getRemoteHost() + ":" + servletRequest.getRemotePort();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}
	
	@Override
	public void destroy() {
	}

}