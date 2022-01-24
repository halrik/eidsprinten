package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.model.enums.Template.ADVANCEMENT_SETUP;
import static com.halrik.eidsprinten.model.enums.Template.RESULT_LIST;
import static com.halrik.eidsprinten.model.enums.Template.START_LIST_FINALS;
import static com.halrik.eidsprinten.model.enums.Template.START_LIST_HEATS;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Result;
import com.halrik.eidsprinten.model.enums.Group;
import java.util.List;
import java.util.stream.Collectors;
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

    public String getStartListFinalsHtml(List<Heat> heats, String startListType) {
        Context context = new Context();
        context.setVariable("startListType", startListType);
        context.setVariable("heats", heats);
        return parseThymeleafTemplate(START_LIST_FINALS.getTemplateName(), context);
    }

    public String getResultListHtml(List<Result> resultList, String groupName) {
        Context context = new Context();
        context.setVariable("groupName", groupName);
        context.setVariable("resultList", resultList);
        return parseThymeleafTemplate(RESULT_LIST.getTemplateName(), context);
    }

    public String getAdvancementsSetupHtml(List<HeatAdvancement> advancements) {
        Context context = new Context();
        context.setVariable("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_11));
        context.setVariable("advancementsGirls11", getAdvancementsForGroup(advancements, Group.GIRLS_11));
        context.setVariable("advancementsBoys12", getAdvancementsForGroup(advancements, Group.BOYS_12));
        context.setVariable("advancementsGirls12", getAdvancementsForGroup(advancements, Group.GIRLS_12));
        context.setVariable("advancementsBoys13", getAdvancementsForGroup(advancements, Group.BOYS_13));
        context.setVariable("advancementsGirls13", getAdvancementsForGroup(advancements, Group.GIRLS_13));
        context.setVariable("advancementsBoys14", getAdvancementsForGroup(advancements, Group.BOYS_14));
        context.setVariable("advancementsGirls14", getAdvancementsForGroup(advancements, Group.GIRLS_14));
        return parseThymeleafTemplate(ADVANCEMENT_SETUP.getTemplateName(), context);
    }

    private List<HeatAdvancement> getAdvancementsForGroup(List<HeatAdvancement> advancements, Group group) {
        return advancements.stream()
            .filter(heatAdvancement -> heatAdvancement.getGroupName().equals(group.getValue()))
            .collect(Collectors.toList());
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
