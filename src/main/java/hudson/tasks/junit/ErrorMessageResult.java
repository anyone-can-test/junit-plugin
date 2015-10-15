package hudson.tasks.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.export.Exported;

/**
 * One error message including case results which have similar error message.
 */
public class ErrorMessageResult {
    private static final Logger LOGGER = Logger.getLogger(ErrorMessageResult.class.getName());
    
    private final String errorMessage;
    
    /**
     * Case results which have similar error message.
     */
    private final List<CaseResult> cases = new ArrayList<CaseResult>();
    
    
    public ErrorMessageResult(String errorMessage) {
    	this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public String getList() {
    	String l = new String();
    	for(CaseResult r: cases) {
    		l += r.getFullName() + "<br>";
    	}
    	
    	return l;
    }
    
    public String getId() {
    	return cases.get(0).getFullName();
    }

    public List<CaseResult> getChildren() {
        return cases;
    }

    public boolean hasChildren() {
        return ((cases != null) && (cases.size() > 0));
    }
    
    public void add(CaseResult r) {
        cases.add(r);
    }
    
    public CaseResult getCaseResult(String name) {
        for (CaseResult c : cases) {
            if(c.getSafeName().equals(name))
                return c;
        }
        return null;
    }
    
    public String getFailedTestList() {
    	return "you!!!";
    }
    
    public int getCount() {
    	return cases.size();
    }

   /* 
    public static boolean isSimilar(CaseResult r1, CaseResult r2) {
    	
		if(StringUtils.getJaroWinklerDistance(r1.getShortErrorMessage(), r2.getShortErrorMessage()) >= 0.9) {
			return true;
		}else {
			return false;
		}
    }
    */
}
