package origin.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping(path = {"test"})
public class UserDefinesController {
    private ExecutorService es = Executors.newFixedThreadPool(2);

    @GetMapping("/de")
    @ResponseBody
    public DeferredResult<ResponseEntity<?>> vedioList(Integer key
//            ,@RequestHeader(value = HttpHeaders.CONTENT_LENGTH, required = true) Long contentLength
    ) {
        List<Map<String, Object>> tmp = new ArrayList<>();
        DeferredResult dr = new DeferredResult(TimeUnit.SECONDS.toMillis(4), new ResponseEntity<>(Arrays.asList("timeout-------"), HttpStatus.OK));
        StopWatch sw = new StopWatch();
        sw.start("b");
        dr.onTimeout(() -> {
            sw.stop();
            System.out.println(sw.toString() + "============timeout==========================");
        });
        dr.onError(e -> {
            System.err.println("err---->" + ExceptionUtils.getStackTrace((Throwable) e));
        });
        dr.onCompletion(() -> {
            sw.stop();
            System.out.println(sw.toString() + ">>>>>>>>>>done>>>>>>>>>>>>>>");
        });

        es.submit(() -> {
            List<String> content = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String s = " --> processing==== :" + i;
                content.add(s);
            }
            dr.setResult(new ResponseEntity<>(content, HttpStatus.OK));
        });
        return dr;
    }

    @GetMapping("/re")
    @ResponseBody
    public ResponseEntity<?> regular(Integer key
//            ,@RequestHeader(value = HttpHeaders.CONTENT_LENGTH, required = true) Long contentLength
    ) {
        List<Map<String, Object>> tmp = new ArrayList<>();
//        DeferredResult dr = new DeferredResult(TimeUnit.SECONDS.toMillis(4), new ResponseEntity<>(Arrays.asList("timeout-------"), HttpStatus.OK));
        StopWatch sw = new StopWatch();
        sw.start("b");
//        dr.onTimeout(() -> {
//            sw.stop();
//            System.out.println(sw.toString() + "============timeout==========================");
//        });
//        dr.onError(e -> {
//            System.err.println("err---->" + ExceptionUtils.getStackTrace((Throwable) e));
//        });
//        dr.onCompletion(() -> {
//            sw.stop();
//            System.out.println(sw.toString() + ">>>>>>>>>>done>>>>>>>>>>>>>>");
//        });
        List<String> content = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String s = " --> processing==== :" + i;
            content.add(s);
        }
        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
