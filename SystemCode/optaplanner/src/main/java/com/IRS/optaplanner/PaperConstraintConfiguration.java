package com.IRS.optaplanner;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@ConstraintConfiguration(constraintPackage = "com.IRS.optaplanner")
public class PaperConstraintConfiguration {
    public static final String AUTHORCONSTRIANT = "AuthorConstriant";
    public static final String CATEGORYCONSTRIANT = "CategoryConstriant";
    public static final String DATECONSTRIANT = "DateConstriant";
    public static final String TITLECONSTRIANT = "TitleConstriant";
    public static final String SOURCECONSTRIANT = "SourceConstriant";
    
    @ConstraintWeight(AUTHORCONSTRIANT)
    private HardSoftScore AuthorConstriant = HardSoftScore.ofSoft(1);
    
    @ConstraintWeight(CATEGORYCONSTRIANT)
    private HardSoftScore CategoryConstriant = HardSoftScore.ofSoft(1);
    
    @ConstraintWeight(DATECONSTRIANT)
    private HardSoftScore DateConstriant = HardSoftScore.ofSoft(1);
    
    @ConstraintWeight(TITLECONSTRIANT)
    private HardSoftScore TitleConstriant = HardSoftScore.ofSoft(1);
    
    @ConstraintWeight(SOURCECONSTRIANT)
    private HardSoftScore SourceConstriant = HardSoftScore.ofSoft(1);

	public HardSoftScore getAuthorConstriant() {
		return AuthorConstriant;
	}

	public void setAuthorConstriant(HardSoftScore authorConstriant) {
		AuthorConstriant = authorConstriant;
	}
	

	
	public HardSoftScore getCategoryConstriant() {
		return CategoryConstriant;
	}

	public void setCategoryConstriant(HardSoftScore categoryConstriant) {
		CategoryConstriant = categoryConstriant;
	}

	public HardSoftScore getDateConstriant() {
		return DateConstriant;
	}

	public void setDateConstriant(HardSoftScore dateConstriant) {
		DateConstriant = dateConstriant;
	}

	
	
	public HardSoftScore getTitleConstriant() {
		return TitleConstriant;
	}

	public void setTitleConstriant(HardSoftScore titleConstriant) {
		TitleConstriant = titleConstriant;
	}

	public HardSoftScore getSourceConstriant() {
		return SourceConstriant;
	}

	public void setSourceConstriant(HardSoftScore sourceConstriant) {
		SourceConstriant = sourceConstriant;
	}

	public PaperConstraintConfiguration() {
	}
    
}
