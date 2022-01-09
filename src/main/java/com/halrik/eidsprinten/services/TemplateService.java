package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.model.enums.TemplateName.START_LIST_HEATS;

import com.halrik.eidsprinten.domain.Heat;
import java.util.List;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class TemplateService {

    public String getStartListHeatsHtml(List<Heat> heats, String startListType) {
        Context context = new Context();
        context.setVariable("startListType", startListType);
        context.setVariable("heats", heats);
        return parseThymeleafTemplate(START_LIST_HEATS.getTemplateName(), context);
    }

    public String parseThymeleafTemplate(String templateName, Context context) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(templateName, context);
    }

}
