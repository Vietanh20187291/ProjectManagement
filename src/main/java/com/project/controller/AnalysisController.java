package com.project.controller;
import com.itextpdf.text.DocumentException;
import com.project.entity.*;
import com.project.entity.enums.UserRole;
import com.project.helper.CookieHelper;
import com.project.service.EmployeeAnalysisService;
import com.project.service.ProjectVersionAnalysisService;
import com.project.service.ProjectVersionService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnalysisController {

    @Autowired
    private EmployeeAnalysisService employeeAnalysisService;
    @Autowired
    private ProjectVersionService projectVersionService;
    @Autowired
    private ProjectVersionAnalysisService projectVersionAnalysisService;
    @Autowired
    private UserService userService;
    @Autowired
    private CookieHelper cookieHelper;

    @GetMapping("/analysis")
    public String getAnalysisMainPage(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        return "analysis/MainPage";
    }

    @GetMapping("/analysis/user")
    public String getUserAnalysis(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<EmployeeAnalysis> employeeAnalyses = new ArrayList<>();

        for (UserDTO user : users) {
            EmployeeAnalysis employeeAnalysis = employeeAnalysisService.getEmployeeAnalysis(user.getUserId());
            employeeAnalyses.add(employeeAnalysis);
        }

        model.addAttribute("employeeAnalyses", employeeAnalyses);

        return "analysis/EmployeeAnalysis";
    }
    @GetMapping("/analysis/pie-chart/user")
    public String getUserAnalysisPieChart(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<Map<String, Object>> employeeTasks = new ArrayList<>();

        for (UserDTO user : users) {
            int employeeId = user.getUserId();
            int tasksCount = employeeAnalysisService.countTasksByEmployeeId(employeeId);
            String employeeName = user.getUsername();
            System.out.println(employeeId+":"+tasksCount);
            Map<String, Object> employeeTaskMap = new HashMap<>();
            employeeTaskMap.put("userName", employeeName);
            employeeTaskMap.put("taskCount", tasksCount);
            employeeTasks.add(employeeTaskMap);
        }

        model.addAttribute("employeeTasks", employeeTasks);

        int totalTasks = employeeAnalysisService.getTotalTask();
        System.out.println(totalTasks);
        model.addAttribute("totalTasks", totalTasks);

        return "analysis/EmployeePieChart";
    }
    @GetMapping("/analysis/projectVersion")
    public String getProjectVersionAnalysis(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);

        List<ProjectVersion> projectVersions = projectVersionService.findAll();
        List<ProjectVersionAnalysis> projectVersionAnalyses = new ArrayList<>();

        for (ProjectVersion projectVersion : projectVersions) {
            ProjectVersionAnalysis projectVersionAnalysis = projectVersionAnalysisService.getProjectVersionAnalysis(projectVersion.getProjectVersionId());
            projectVersionAnalyses.add(projectVersionAnalysis);
        }

        model.addAttribute("projectVersionAnalyses", projectVersionAnalyses);

        return "analysis/VersionAnalysis";
    }
//    @PostMapping("/analysis/user/download")
//    public ResponseEntity<FileSystemResource> downloadEmployeeAnalysis(RedirectAttributes redirectAttributes) {
//        // TODO: Implement logic to retrieve employee analysis data from the list of EmployeeAnalysis
//        List<EmployeeAnalysis> employeeAnalyses = // Your code to get the list of EmployeeAnalysis
//
//                String fileName = "employee_analysis_report.pdf";
//        String fole  = "employee_analysis_report.pdf";
//
//        try {
//            employeeAnalysisService.generateReport(employeeAnalyses);
//            redirectAttributes.addFlashAttribute("message", "Báo cáo đã được tạo và tải xuống thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo báo cáo: " + e.getMessage());
//        }
//
//        // Tạo đường dẫn tới file PDF đã được tạo
//        String pathToDownload = "/path/to/your/directory/" + fileName;
//        FileSystemResource file = new FileSystemResource(pathToDownload);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//
//        // Trả về response với file PDF để trình duyệt có thể tải xuống
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(file);
//    }

}

