package com.halrik.eidsprinten.resources;

import static com.halrik.eidsprinten.utils.HeatsUtil.getAdvancementsForGroup;
import static com.halrik.eidsprinten.utils.HeatsUtil.getHeatsRankedFinalsForGroup;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.services.FinalHeatsService;
import com.halrik.eidsprinten.services.HeatsService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private HeatsService heatsService;
    private FinalHeatsService finalHeatsService;

    public WebController(HeatsService heatsService, FinalHeatsService finalHeatsService) {
        this.heatsService = heatsService;
        this.finalHeatsService = finalHeatsService;
    }

    @GetMapping("/")
    public String index() {
        return "web/index.html";
    }

    @GetMapping("/startliste-urangerte")
    public String startListUnranked(Model model) {
        List<Heat> heatsUnRanked = heatsService.getHeatsUnRankedStored();
        model.addAttribute("title", "Startliste urangerte");
        model.addAttribute("heats", heatsUnRanked);
        return "web/startlist.html";
    }

    @GetMapping("/startliste-rangerte")
    public String startListRanked(Model model) {
        List<Heat> heatsRanked = heatsService.getHeatsRankedStored();
        model.addAttribute("title", "Startliste rangerte");
        model.addAttribute("heats", heatsRanked);
        return "web/startlist.html";
    }

    @GetMapping("/startlister-finaler")
    public String startlistFinalsMenu() {
        return "web/startlist-finals-menu.html";
    }

    @GetMapping("/startliste-finale")
    public String startlistFinal(@RequestParam(name = "group", required = false) String group, Model model) {
        List<Heat> heatsRankedFinals = finalHeatsService.getHeatsRankedFinalsStored();

        if (group != null) {
            switch (group) {
                case "G11":
                    model.addAttribute("title", "Startliste finaler " + Group.BOYS_11.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.BOYS_11));
                    return "web/startlist-finals.html";
                case "J11":
                    model.addAttribute("title", "Startliste finaler " + Group.GIRLS_11.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.GIRLS_11));
                    return "web/startlist-finals.html";
                case "G12":
                    model.addAttribute("title", "Startliste finaler " + Group.BOYS_12.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.BOYS_12));
                    return "web/startlist-finals.html";
                case "J12":
                    model.addAttribute("title", "Startliste finaler " + Group.GIRLS_12.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.GIRLS_12));
                    return "web/startlist-finals.html";
                case "G13":
                    model.addAttribute("title", "Startliste finaler " + Group.BOYS_13.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.BOYS_13));
                    return "web/startlist-finals.html";
                case "J13":
                    model.addAttribute("title", "Startliste finaler " + Group.GIRLS_13.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.GIRLS_13));
                    return "web/startlist-finals.html";
                case "G14":
                    model.addAttribute("title", "Startliste finaler " + Group.BOYS_14.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.BOYS_14));
                    return "web/startlist-finals.html";
                case "J14":
                    model.addAttribute("title", "Startliste finaler " + Group.GIRLS_14.getValue());
                    model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, Group.GIRLS_14));
                    return "web/startlist-finals.html";
            }
        }
        return "web/startlist-finals-menu.html";
    }

    @GetMapping("/avansement-meny")
    public String advancementSetupMenu() {
        return "web/menu-advancement-setup.html";
    }

    @GetMapping("/registrer-resultat")
    public String registerResult() {
        return "web/register-result.html";
    }

    @GetMapping("/avansement")
    public String advancementSetup(@RequestParam(name = "group", required = false) String group, Model model) {
        List<HeatAdvancement> advancements = finalHeatsService.getAdvancementSetup();

        if (group != null) {
            switch (group) {
                case "G11":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_11));
                    return "web/advancement-setup.html";
                case "J11":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.GIRLS_11));
                    return "web/advancement-setup.html";
                case "G12":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_12));
                    return "web/advancement-setup.html";
                case "J12":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.GIRLS_12));
                    return "web/advancement-setup.html";
                case "G13":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_13));
                    return "web/advancement-setup.html";
                case "J13":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.GIRLS_13));
                    return "web/advancement-setup.html";
                case "G14":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_14));
                    return "web/advancement-setup.html";
                case "J14":
                    model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.GIRLS_14));
                    return "web/advancement-setup.html";
            }
        }

        model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, Group.BOYS_11));
        model.addAttribute("advancementsGirls11", getAdvancementsForGroup(advancements, Group.GIRLS_11));
        model.addAttribute("advancementsBoys12", getAdvancementsForGroup(advancements, Group.BOYS_12));
        model.addAttribute("advancementsGirls12", getAdvancementsForGroup(advancements, Group.GIRLS_12));
        model.addAttribute("advancementsBoys13", getAdvancementsForGroup(advancements, Group.BOYS_13));
        model.addAttribute("advancementsGirls13", getAdvancementsForGroup(advancements, Group.GIRLS_13));
        model.addAttribute("advancementsBoys14", getAdvancementsForGroup(advancements, Group.BOYS_14));
        model.addAttribute("advancementsGirls14", getAdvancementsForGroup(advancements, Group.GIRLS_14));

        return "web/advancement-setup.html";
    }

}

