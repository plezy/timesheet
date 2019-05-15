package lu.plezy.timesheet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("passw0rd");
        System.out.println(encodedPassword);
    }

    // Test le login valide
    @Test
    public void testLogon() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("username", "admin");
        jo.put("password", "passw0rd");

        System.out.println("mockMvc object : " + mockMvc);
        mockMvc.perform(post("/auth/logon").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(jo.toString())).andExpect(status().isOk()).andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    System.out.println("JSON Result : " + json);
                });

    }

    // Test le login en erruer
    @Test
    public void testLogonError() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("username", "admin");
        jo.put("password", "badPassword");

        System.out.println("mockMvc object : " + mockMvc);
        mockMvc.perform(post("/auth/logon").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(jo.toString())).andExpect(status().is4xxClientError());

    }

}