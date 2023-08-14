package origin.spring.controller;

//import org.springframework.boot.autoconfigure.web.BasicErrorController;
//import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BasicErrorResolver extends BasicErrorController {

    private static final String PATH = "/error";

    public BasicErrorResolver() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> rj = new HashMap<>();
//        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
//        HttpStatus status = getStatus(request);
//        rj.put("errorCode", status.value());
//        rj.put("msg", body.get("message"));
//        rj.put("data", body);
        return new ResponseEntity<>(rj, HttpStatus.OK);
    }

//    @Override
    public String getErrorPath() {
        return PATH;
    }
}

