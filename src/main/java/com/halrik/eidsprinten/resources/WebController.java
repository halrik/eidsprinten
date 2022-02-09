package com.halrik.eidsprinten.resources;

import static com.halrik.eidsprinten.utils.HeatsUtil.getAdvancementsForGroup;
import static com.halrik.eidsprinten.utils.HeatsUtil.getHeatsRankedFinalsForGroup;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Result;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.services.EidsprintenService;
import com.halrik.eidsprinten.services.FinalHeatsService;
import com.halrik.eidsprinten.services.HeatsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private EidsprintenService eidsprintenService;
    private HeatsService heatsService;
    private FinalHeatsService finalHeatsService;

    public WebController(EidsprintenService eidsprintenService,
        HeatsService heatsService, FinalHeatsService finalHeatsService) {
        this.eidsprintenService = eidsprintenService;
        this.heatsService = heatsService;
        this.finalHeatsService = finalHeatsService;
    }

    @GetMapping("/")
    public String index() {
        return "web/index.html";
    }

    @GetMapping("/starttider")
    public String startTime(Model model) {
        Map<String, String> startTimeMap = new HashMap<>();
        model.addAttribute("title", "Startider per klasse");
        model.addAttribute("startTimeMap", heatsService.getStartTimeMap());
        return "web/starttime.html";
    }

    @GetMapping("/startnummerliste")
    public String startNumbers(Model model) {
        Map<String, List<Team>> teamsGroupedByClub = eidsprintenService.getTeamsGroupedByClub();
        model.addAttribute("title", "Startnummer sortert per klubb");
        model.addAttribute("teamsGroupedByClub", teamsGroupedByClub);
        return "web/startnumbers.html";
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
    public String startListFinalsMenu() {
        return "web/startlist-finals-menu.html";
    }

    @GetMapping("/startliste-finale")
    public String startListFinal(@RequestParam(name = "group", required = false) String genderAgeShortValue,
        Model model) {
        if (genderAgeShortValue != null) {
            Group group = Group.valueOfGenderAgeShortValue(genderAgeShortValue);
            List<Heat> heatsRankedFinals = finalHeatsService.getHeatsRankedFinalsStored();
            model.addAttribute("title", "Startliste finaler " + group.getValue());
            model.addAttribute("heats", getHeatsRankedFinalsForGroup(heatsRankedFinals, group));
            return "web/startlist-finals.html";
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
    public String advancementSetup(@RequestParam(name = "group", required = false) String genderAgeShortValue,
        Model model) {
        List<HeatAdvancement> advancements = finalHeatsService.getAdvancementSetup();

        if (genderAgeShortValue != null) {
            Group group = Group.valueOfGenderAgeShortValue(genderAgeShortValue);
            model.addAttribute("advancementsBoys11", getAdvancementsForGroup(advancements, group));
            return "web/advancement-setup.html";
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

    @GetMapping("/resultater")
    public String resultList(@RequestParam(name = "group", required = false) String genderAgeShortValue, Model model) {
        if (genderAgeShortValue != null) {
            Group group = Group.valueOfGenderAgeShortValue(genderAgeShortValue);
            List<Result> resultList = finalHeatsService.getHeatsRankedResults(group);
            model.addAttribute("title", "Resultatliste for " + group.getValue());
            model.addAttribute("resultList", resultList);
            return "web/resultlist.html";
        }

        return "web/resultlists.html";
    }

    @GetMapping("/premieutdeling")
    public String awardCeremony(Model model) {
        Map<String, String> startTimeMap = new HashMap<>();
        model.addAttribute("title", "Premieutdeling");
        model.addAttribute("awardCeremonyTimeMap", heatsService.getAwardCeremonyTimeMap());
        return "web/awardceremony.html";
    }

}

