package origin.web;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/4
 * @Desc:
 **/

@WebServlet(urlPatterns = "/demo", asyncSupported = true)
public class WebServletTest extends HttpServlet {

    private Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Do Something
        AsyncContext ctx = req.startAsync();
        startAsyncTask(ctx);
    }

    private void startAsyncTask(AsyncContext ctx) {
//        requestRpcService(result -> {
//            try {
//                PrintWriter out = ctx.getResponse().getWriter();
//                out.println(result);
//                out.flush();
//                ctx.complete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void requestRpcService(String o) {

    }

    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model) {
        log.info("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            log.info("Processing in separate thread");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok("ok"));
        });
        log.info("servlet thread freed");
        return output;
    }
}
