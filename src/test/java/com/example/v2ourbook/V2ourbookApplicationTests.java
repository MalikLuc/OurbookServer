package com.example.v2ourbook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@ContextConfiguration(classes=BorrowBookTest.class)
//@WebMvcTest(RequestController.class)
//class V2ourbookApplicationTests {
//    @MockBean
//    private RequestController requestController;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @WithMockUser("spring")
//    @Test
//    void newRequest() throws Exception {
//        mockMvc.perform(get("/requests/getBookRequests")
//                .contentType("application/json"))
//                .andExpect(status().isOk());
//        RestGetBookRequest req = new RestGetBookRequest();
//        req.setBookId(5001L);
//        Long ret = requestController.newGetBookRequest(req);
//        System.out.println("RET: "+ ret);
//    }
//}