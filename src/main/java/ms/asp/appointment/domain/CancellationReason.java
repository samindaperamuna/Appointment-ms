package ms.asp.appointment.domain;

public enum  CancellationReason{

  //	Patient
  	CanceledViaAutomatedReminderSystem ("pat-crs"),
  	CanceledViaPatientPortal ("pat-cpp"),
  	Deceased ("pat-dec"),
  	FeelingBetter ("pat-fb"),
  	LackOfTransportation ("pat-lt"),
  	MemberTerminated ("pat-mt"),
  	Moved ("pat-mv"),
  	Pregnant ("pat-preg"),
  	ScheduledFromWaitList ("pat-swl"),
  	UnhappyChangedProvider ("pat-ucp"),

  // 	Provider
  	Personal ("prov-pers"),
  	Discharged ("prov-dch"),
  	EduMeeting ("prov-edu"),
  	Hospitalized ("prov-hosp"),
  	LabsOutOfAcceptableRange ("prov-labs"),
  	MRIScreeningFormMarkedDoNotProceed ("prov-mri"),
  	OncologyTreatmentPlanChanges ("prov-onc"),
    EquipmentMaintenanceRepair("maint"),
  	PrepMedIncomplete ("meds-inc"),

  //	Other
  	CMSTherapyCapServiceNotAuthorized ("oth-cms"),
  	Error ("oth-err"),
  	Financial ("oth-fin"),
  	ImproperIVAccessInfiltrateIV ("oth-iv"),
  	NoInterpreterAvailable ("oth-int"),
  	PrepMedResultsUnavailable ("oth-mu"),
  	RoomResourceMaintenance ("oth-room"),
  	ScheduleOrderError ("oth-oerr"),
  	SilentWalkInError ("oth-swie"),
  	Weather ("oth-weath");


  	CancellationReason(String value){
  	  this.value = value;
      }
  private final String value;

}
