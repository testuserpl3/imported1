package com.atlassian.stash.plugin.servlet;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.project.Project;
import com.atlassian.stash.project.ProjectService;
import com.atlassian.stash.user.StashUser;
import com.atlassian.stash.user.UserService;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ProjectServlet extends HttpServlet {
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final ProjectService projectService;

    public ProjectServlet(SoyTemplateRenderer soyTemplateRenderer, ProjectService projectService) {
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.projectService = projectService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get projectKey from path
        String pathInfo = req.getPathInfo();

        String projectKey = pathInfo.substring(1); // Strip leading slash
        Project project = projectService.getByKey(projectKey);

        if (project == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        render(resp, "plugin.example.project", ImmutableMap.<String, Object>of("project", project));
    }

    private void render(HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(resp.getWriter(),
                    "com.atlassian.stash.plugin.stash-example-plugin:example-soy",
                    templateName,
                    data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

}
