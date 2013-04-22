package com.atlassian.stash.plugin.servlet;

import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.project.Project;
import com.atlassian.stash.project.ProjectService;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectServlet extends AbstractExampleServlet {
    private final ProjectService projectService;

    public ProjectServlet(SoyTemplateRenderer soyTemplateRenderer, ProjectService projectService) {
        super(soyTemplateRenderer);
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
}
