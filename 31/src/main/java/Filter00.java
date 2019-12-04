import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//注解/过滤器名称/该过滤器对所有请求有效
@WebFilter(filterName = "Filter 0",urlPatterns = "/*")
public class Filter00 implements Filter {
    private Set<String> toExcludes = new HashSet<String>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        toExcludes.add("/login");
        toExcludes.add("/app");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //打印提示信息
        System.out.println("EncodingFilter -encoding begins");
        //强制类型转换成HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获得请求的url
        String path = request.getRequestURI();
        //如果请求路径不含"/login"
        for (String toExclude:toExcludes) {
            if (!path.contains(toExclude)) {
                //打印提示信息
                System.out.println("set response");
                //设置响应字符编码格式
                response.setContentType("text/html;charset=UTF-8");
                //获得请求的方法
                String method = request.getMethod();
                //如果请求方法是POST或PUT
                if ("POST-PUT".contains(method)) {
                    //设置请求字符编码为UTF-8
                    request.setCharacterEncoding("UTF-8");
                }
            }
        }
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("EncodingFilter -encoding ends");
    }

    @Override
    public void destroy() {}
}
