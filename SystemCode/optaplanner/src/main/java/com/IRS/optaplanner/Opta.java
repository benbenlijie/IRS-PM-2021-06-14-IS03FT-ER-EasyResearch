package com.IRS.optaplanner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.api.runtime.process.ProcessInstance;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

public class Opta {
	
	public void recommend(User user, String filename) throws Exception {
		//Start Opta process
        PaperDAO itemsDao = new PaperDAOimpl();
        List<Paper> paperList = itemsDao.findAll();

        SolverFactory<Solution> solverFactory =SolverFactory.createFromXmlResource("SolverConfig.solver.xml");
		Solver<Solution> solver = solverFactory.buildSolver();
        PaperConstraintConfiguration paperConstraintConfiguration=new PaperConstraintConfiguration();
        paperConstraintConfiguration.setAuthorConstriant(HardSoftScore.ofSoft(1));
        paperConstraintConfiguration.setCategoryConstriant(HardSoftScore.ofHard(1));
        paperConstraintConfiguration.setDateConstriant(HardSoftScore.ofSoft(1));
        paperConstraintConfiguration.setTitleConstriant(HardSoftScore.ofSoft(1));
        paperConstraintConfiguration.setSourceConstriant(HardSoftScore.ofSoft(1));
        
        Solution solution= new Solution(Arrays.asList(user), paperList, paperConstraintConfiguration);
        
        //Get Best solution
        solver.solve(solution);
        Paper bestPaper = solver.getBestSolution().getUserList().get(0).getPaper();
        
        //Write to file
        writeToFile(filename, bestPaper);

        
	}
	
	public void writeToFile(String filename, Paper bestPaper) {
		String name = Arrays.asList(filename.split("input.txt")).get(0)+"results.txt";

		filename = name;
        Path file = Paths.get(filename);
        String title = bestPaper.getTitle().replaceAll("[\\t\\n\\r]", "");
        String authors = bestPaper.getAuthors().replaceAll("[\\t\\n\\r]", "").replace(".", " ").replace(',', '_');
        String category = bestPaper.getCategory().replaceAll("[\\t\\n\\r]", "");
        String date = bestPaper.getDate().replaceAll("[\\t\\n\\r]", "");
        String url = "https://arxiv.org/abs/" + bestPaper.getPaperid().replaceAll("[\\t\\n\\r]", "");
        String head = "title,author,date,url";
        String line = title + "," + authors + "," + date + "," + url;
        try {
//			Files.write(file, Arrays.asList(head,line), StandardCharsets.UTF_8);
			System.out.println(head);
			System.out.println(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
