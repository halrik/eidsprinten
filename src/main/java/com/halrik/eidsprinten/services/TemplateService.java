package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.model.enums.TemplateName.START_LIST_UNRANKED;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class TemplateService {

    public String getStartListUnrankedHtml() {
        Context context = new Context();
        return parseThymeleafTemplate(START_LIST_UNRANKED.getTemplateName(), context);
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
