package edu.stevens.cs548.clinic.billing;

import edu.stevens.cs548.clinic.domain.Treatment;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-03-17T12:04:39.097-0400")
@StaticMetamodel(BillingRecord.class)
public class BillingRecord_ {
	public static volatile SingularAttribute<BillingRecord, Long> id;
	public static volatile SingularAttribute<BillingRecord, String> description;
	public static volatile SingularAttribute<BillingRecord, Date> date;
	public static volatile SingularAttribute<BillingRecord, Float> amount;
	public static volatile SingularAttribute<BillingRecord, Treatment> treatment;
}
