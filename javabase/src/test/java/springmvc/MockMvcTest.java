package springmvc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
//import origin.Main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author muqi.lmq
 * @date 2018/6/20.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Main.class, properties = {"origin.spring.config.location=classpath:/data/,classpath:/", "origin.spring.config.name=application,datas,redis-config"})
public class MockMvcTest {
//    @Autowired
//    private WebApplicationContext wac;
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//    @Test
//    public void jobInfoSelect() throws Exception {
//        this.mockMvc.perform(get("/job/select")).andDo(log()).andExpect(status().isOk());
//    }
//
//    public void test() throws Exception {
//        MockHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.get("/test");
//
//
//        this.mockMvc.perform(builder)
//                .andExpect(MockMvcResultMatchers.request()
//                        .asyncStarted())
//                .andExpect(MockMvcResultMatchers.request()
//                        .asyncResult("async result"))
//                .andExpect(MockMvcResultMatchers.status()
//                        .isOk());
//    }
}
