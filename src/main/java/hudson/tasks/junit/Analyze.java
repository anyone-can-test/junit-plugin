/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Tom Huybrechts, Yahoo!, Inc., Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.tasks.junit;

import hudson.tasks.test.TestObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 */
public class Analyze {
	private final TestObject testObject;

	/**
	 * All {@link ErrorMessageResult}
	 */
	private final HashMap<String, ErrorMessageResult> errorMessages;

	public Analyze(TestObject testObject) {
		this.testObject = testObject;

		errorMessages = new HashMap<String, ErrorMessageResult>();
		
		// make error message
		List<CaseResult> cases = (List<CaseResult>) testObject.getResultInRun(testObject.getRun()).getFailedTests();
		for(CaseResult cr: cases) {
			String newErrorMsg = cr.getShortErrorMessage();
			
			boolean bSimilar = false;
			for(String contained: errorMessages.keySet()) {
				if(StringUtils.getJaroWinklerDistance(newErrorMsg, contained) >= 0.9) {
					bSimilar = true;
				
					// add case to the existing error message result (similar)
					ErrorMessageResult errMsgResult = errorMessages.get(contained);
					errMsgResult.add(cr);
					break;
				}
			}
			if(bSimilar == false) {
				// add a new error message result
				ErrorMessageResult errMsgResult = new ErrorMessageResult(newErrorMsg);
				errMsgResult.add(cr);
				errorMessages.put(newErrorMsg, errMsgResult);
			}
		}
	
	}
	

	public TestObject getTestObject() {
		return testObject;
	}

	

	/**
	 * 
	 * @return The set of short error messages (not duplicate)
	 */
	public Set<String> getShortErrorMessageList() {
		return errorMessages.keySet();
	}

	/**
	 * 
	 * @return
	 */
    public List<ErrorMessageResult> getResults() {
        return new ArrayList<ErrorMessageResult>(errorMessages.values());
    }
	
}