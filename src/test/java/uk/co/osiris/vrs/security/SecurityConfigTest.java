package uk.co.osiris.vrs.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.osiris.vrs.company.AdminCompanyController;
import uk.co.osiris.vrs.company.CompanyDto;
import uk.co.osiris.vrs.company.CompanyService;
import uk.co.osiris.vrs.company.UserCompanyController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserCompanyController.class, AdminCompanyController.class})
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CompanyService companyService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        var company = new CompanyDto();
        company.setId(1L);
        company.setName("Test");
        company.setEmail("test@example.com");
        when(companyService.findById(1L)).thenReturn(company);
        when(companyService.findByUserEmail(eq("user"))).thenReturn(company);
        when(companyService.create(any())).thenReturn(company);
    }

    @Test
    void authPathsArePermittedWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/auth/status"))
                .andExpect(status().isNotFound());
    }

    @Test
    void userPathsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/user/v1/companies/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void adminPathsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/admin/v1/companies"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void userWithRoleUserCanAccessUserPaths() throws Exception {
        mockMvc.perform(get("/user/v1/companies/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void userWithRoleUserCanAccessCurrentCompany() throws Exception {
        mockMvc.perform(get("/user/v1/companies/me")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void userWithRoleUserCannotAccessAdminPaths() throws Exception {
        mockMvc.perform(post("/admin/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\"}")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void userWithRoleAdminCanAccessAdminPaths() throws Exception {
        mockMvc.perform(post("/admin/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"email\":\"test@example.com\"}")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isCreated());
    }
}
