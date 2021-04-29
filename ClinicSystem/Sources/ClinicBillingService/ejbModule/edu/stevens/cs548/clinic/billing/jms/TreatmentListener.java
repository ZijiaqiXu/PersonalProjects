package edu.stevens.cs548.clinic.billing.jms;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import edu.stevens.cs548.clinic.billing.service.BillingDtoFactory;
import edu.stevens.cs548.clinic.billing.service.IBillingService;
import edu.stevens.cs548.clinic.billing.service.IBillingService.BillingDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Message-Driven Bean implementation class for: TreatmentListener
 *
 */
@MessageDriven (
	mappedName = "jms/Treatment",
	activationConfig = {
		@ActivationConfigProperty (
			propertyName = "destinationType", 
			propertyValue = "javax.jms.topic"
		)
	}
)
public class TreatmentListener implements MessageListener {
	
	private static Logger logger = Logger.getLogger(TreatmentListener.class.getCanonicalName());

    /**
     * Default constructor. 
     */
    public TreatmentListener() {
        billingDtoFactory = new BillingDtoFactory();
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    
    @Inject IBillingService billingService;
    
    private BillingDtoFactory billingDtoFactory;
    
    public void onMessage(Message message) {
        ObjectMessage objMessage = (ObjectMessage)message;
        try {
        	TreatmentDto treatment = (TreatmentDto)(objMessage.getObject());
        	
        	logger.info("Billing obtained a treatment record: "+treatment.getDiagnosis());

        	BillingDTO dto = billingDtoFactory.createBillingDTO();
        	dto.setDate(new Date(System.currentTimeMillis()));
        	dto.setDescription(getTreatmentDescription(treatment));
        	dto.setTreatmentId(treatment.getId());
        	
        	Random generator = new Random();
        	float amount = generator.nextFloat() * 500;
        	dto.setAmount(amount);
        	
        	billingService.addBillingRecord(dto);
        } catch (JMSException e) {
        	logger.log(Level.SEVERE, "Failure while consuming JMS message", e);
        }
    }
    
    private static String getTreatmentDescription(TreatmentDto treatment) {
    	if (treatment.getDrugTreatment() != null) {
    		return "Drug treatment with "  + treatment.getDrugTreatment().getDosage() + "g " + treatment.getDrugTreatment().getDrug();
    	} else if (treatment.getSurgeryTreatment() != null) {
    		return "Surgery treatment at " + treatment.getSurgeryTreatment().getDate().toString();
    	} else {
    		StringBuilder sb = new StringBuilder("Radiology treatment at ");
    		for (Date date: treatment.getRadiologyTreatment().getDate()) {
    			sb.append(date.toString()+ "; "); 
    		}
    		return sb.toString();
    	}
    }

}
