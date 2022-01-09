package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.model.enums.TemplateName.START_LIST_UNRANKED;

import com.halrik.eidsprinten.domain.Heat;
import java.util.List;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class TemplateService {

    public String getStartListUnrankedHtml(List<Heat> heatsUnRanked) {
        Context context = new Context();
        context.setVariable("heats", heatsUnRanked);
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
