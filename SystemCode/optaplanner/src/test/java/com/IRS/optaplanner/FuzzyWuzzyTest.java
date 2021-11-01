package com.IRS.optaplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import me.xdrop.fuzzywuzzy.ratios.PartialRatio;

public class FuzzyWuzzyTest {
    static final long serialVersionUID = 1L;
    
//    @Test
//	public void test() throws Exception {
////		// TODO Auto-generated method stub
//
//		KieContainer container = null;
//        boolean success = true;
//        
//        KieServices kieServices = KieServices.Factory.get();
//        container =kieServices.newKieClasspathContainer();
//        KieSession session = container.newKieSession("ksession");
//        
////        CustomHumanTaskHandler humanTaskHandler = new CustomHumanTaskHandler();
////        session.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
//        
//        User user = new User();
//        user.setRequiredAuthors("jerry");
//        user.setRequiredCategory("cs.CL");
//        user.setRequiredTitle("[a]");
//        user.setRequiredDate("2022");
//        user.setIsRule(true);
//        
//        Opta opta = new Opta();
//        opta.recommend(user,"");
//		
//	}
//	
//    private Solution getSolution() {
//        Paper paper = new Paper();
//        paper.setAuthors("jacky");
//
//        Paper paper2 = new Paper();
//        paper2.setAuthors("jerry");
//
//
//
//        List<Paper> paperList=new ArrayList();
//        paperList.add(paper);
//        paperList.add(paper2);
//
//
//        User user = new User();
//        user.setRequiredAuthors("jacky");
//
//
////        ISPConstraintConfiguration constraintConfiguration=new ISPConstraintConfiguration();
////        constraintConfiguration.setHardLastEntryAge(HardSoftScore.ofHard(1));
////        constraintConfiguration.setPreHospitalisationCoveredDays(HardSoftScore.ofSoft(1));
////        constraintConfiguration.setPostHospitalisationCoveredDays(HardSoftScore.ofSoft(1));
//          PaperConstraintConfiguration paperConstraintConfiguration=new PaperConstraintConfiguration();
//          paperConstraintConfiguration.setAuthorConstriant(HardSoftScore.ofSoft(1));
//
//        return new Solution(Arrays.asList(user),
//        		paperList,
//                paperConstraintConfiguration);
//    }

}
