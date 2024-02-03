# scheduler-service

## About
(  Scheduler as a service  )
 -------------------------
       O
	O   ^__^
	 o  (oo)\_______
	    (__)\         )\/\
		|  |----w   |
		|  |      | |



## Help
![SchedulerOverview.png](images%2FSchedulerOverview.png)

[Cron](https://bradymholt.github.io/cron-expression-descriptor/)

### References
[Quartz Scheduler with SpringBoot](https://medium.com/@manvendrapsingh/quartz-scheduling-in-springboot-7cea1b7b19e7)
[quartz-scheduler-daily-mail-subscription-spring-boot](https://github.com/hardikSinghBehl/quartz-scheduler-daily-mail-subscription-spring-boot)
[Quartz-scheduler-email-scheduling](https://www.callicoder.com/spring-boot-quartz-scheduler-email-scheduling-example/)
[Github Code]https://github.com/callicoder/spring-boot-quartz-scheduler-email-scheduling/tree/master/src/main/java/com/example/quartzdemo)
[Removing Triggers programmatically](https://www.tothenew.com/blog/removing-triggers-and-rescheduling-a-quartz-2-job-programatically/)

### Mapping Table names
[Hibernate-JPA Associations](https://www.baeldung.com/jpa-hibernate-associations)
[Entity <-> Table Name mapping](https://www.baeldung.com/jpa-entity-table-names)

### Code with Blob/Clob
[Hibernate Mapping Blob and Clob](https://thorben-janssen.com/mapping-blobs-and-clobs-with-hibernate-and-jpa/)
* Example Attributes: 
 - Clob -- Text -> content 
 - Blob -- byte[] -> cover
```
Book b2 = em.find(Book.class, b.getId());
Reader charStream = b2.getContent().getCharacterStream();
InputStream binaryStream = b2.getCover().getBinaryStream();
```

### ToDo List
[1](https://www.tothenew.com/blog/removing-triggers-and-rescheduling-a-quartz-2-job-programatically/)
```
def unscheduleJob(def jobName) {
    // Find jobKey of job.
    def jobKeys = quartzScheduler.getJobKeys(GroupMatcher.anyJobGroup())
    def jobKey = jobKeys.find {it.name.endsWith(jobName)}
    // Get list of existing triggers.
    def triggersList = quartzScheduler.getTriggersOfJob(jobKey)
    triggersList.each {
        quartzScheduler.unscheduleJob(it.key) // remove all existing triggers
    }
}
```

[Verify Locking with Thread Count](https://stackoverflow.com/questions/2440143/how-to-check-how-many-threads-are-waiting-for-a-synchronized-method-to-become-un)
```
public class Sync {
    public static int waiting = 0;
    private Object mutex = new Object();

    public void sync() {
        waiting++;
        synchronized (mutex) {
            waiting--;
            long start = System.currentTimeMillis();
            doWhatever();
            System.out.println("duration:"
                    + (System.currentTimeMillis() - start));
        }
    }
}
```

### SQL

```
select NOW() - from_unixtime(NEXT_FIRE_TIME/1000),from_unixtime(PREV_FIRE_TIME/1000),
from_unixtime(NEXT_FIRE_TIME/1000),TRIGGER_NAME,TRIGGER_GROUP,JOB_NAME  
from QRTZ_TRIGGERS where TRIGGER_STATE = 'BLOCKED'; 
```

### [h2 database](http://localhost:9091/h2-console/login.do?jsessionid=5ca3dc11c18d1a3c5184ea2a04c6cca7)
```
SELECT * FROM MY_ACTION;

SELECT * FROM EXPIRED_TRIGGER;
SELECT * FROM QRTZ_TRIGGERS;
SELECT * FROM QRTZ_JOB_DETAILS;
SELECT * FROM QRTZ_FIRED_TRIGGERS;

SELECT * FROM QRTZ_LOCKS;
SELECT * FROM QRTZ_SCHEDULER_STATE;
SELECT * FROM QRTZ_CRON_TRIGGERS;
SELECT * FROM QRTZ_SIMPLE_TRIGGERS;

SELECT * FROM CLEAN_BLOG_ACTION;
SELECT * FROM EXCHANGE_USER_ACTION ;
SELECT * FROM EXCHANGE_USER;

SELECT * FROM BLOG;

// DELETE FROM QRTZ_FIRED_TRIGGERS WHERE TRIGGER_NAME  LIKE '%bo%'
```

### REST Endpoint
```
POST http://localhost:9091/jobs/subscribe
{
"blogId":61,
"username": "xcv@foo.com"
}
```

## ToDo: Additional Reading 
### Timezones
[Using Timezones](https://reflectoring.io/spring-timezones/)

### Quartz Locks
[Issue with DB row lock when load is high](https://github.com/quartz-scheduler/quartz/pull/363/files)
[Issue with misfires](https://stackoverflow.com/questions/24809936/quartz-jobstoresupport-recovermisfiredjobs-handling-3-triggers-that-missed-the)

```
INSERT INTO QRTZ_LOCKS values('CauseITBatchScheduler', 'TRIGGER_ACCESS');
INSERT INTO QRTZ_LOCKS values('CauseITBatchScheduler','JOB_ACCESS');
INSERT INTO QRTZ_LOCKS values('CauseITBatchScheduler','CALENDAR_ACCESS');
INSERT INTO QRTZ_LOCKS values('CauseITBatchScheduler','STATE_ACCESS');
INSERT INTO QRTZ_LOCKS values('CauseITBatchScheduler','MISFIRE_ACCESS');
```


## Triggers In Quartz DB
![trigeersInfoInDB.png](images%2FtrigeersInfoInDB.png)


## Misfires
**Q.Possible scenarios for a misfire:**
```
Less number of Scheduler Threads and slow running jobs - could lead to queuing the jobs. 
And consequently misfiring the jobs.
```

**Ques When Quartz Scheduler mentions "Handling trigger(s) that missed their scheduled fire-time," it is referring to a situation where a scheduled job was supposed to be executed at a specific time according to its trigger configuration, but for some reason, it was not able to run at that exact scheduled time. This can happen due to various reasons such as system load, resource constraints, or errors in job execution.**
```
Quartz Scheduler provides a mechanism to handle such scenarios and ensure that the missed triggers are eventually executed. There are a few ways this can be managed:

Misfire Instructions:

Triggers in Quartz have a property called the "misfire instruction," which determines how the scheduler should behave when a trigger misfires (i.e., when it wasn't able to fire at its scheduled time).
Common misfire instructions include:
SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW: The trigger should fire as soon as possible after the misfire.
SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT: The trigger should be rescheduled to fire immediately but with the same repeat count.
CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW: The trigger should fire once as soon as possible after the misfire.
TriggerListener:

You can implement a TriggerListener to be notified when triggers misfire or when they are successfully fired.
By implementing a custom TriggerListener, you can define specific actions to take when a trigger misfires, such as logging the event or taking corrective actions.
Scheduler Configuration:

The Quartz Scheduler itself can be configured with properties related to misfires.
For example, you can set a global misfire threshold that defines the maximum amount of time a trigger can misfire before Quartz considers it a misfire.
Handling missed trigger fire-times is important to ensure the reliability of scheduled jobs. By configuring misfire instructions and utilizing trigger listeners, you can tailor Quartz Scheduler's behavior to meet the specific requirements of your application in the event of missed trigger execution.

```

**Q If nothing is set, what is the default behaviour on misfire of a cron trigger? **
> Property: **org.quartz.jobStore.misfireThreshold**
```
In Quartz Scheduler, if no misfire instruction is explicitly set for a CronTrigger, the default behavior is configured by the org.quartz.jobStore.misfireThreshold property. This property defines the time (in milliseconds) that the scheduler will tolerate for a trigger to misfire before considering it a misfire.

If a CronTrigger misfires and the current time is within the misfire threshold of the trigger's next scheduled fire time, the trigger is considered misfired but will still be scheduled to run immediately.

Here are the default misfire instructions for CronTrigger when the **org.quartz.jobStore.misfireThreshold** property is not explicitly set:

If the misfire threshold has not been reached, the trigger is considered misfired, but it will be rescheduled to fire immediately.
If the misfire threshold has been reached, the trigger will be considered misfired, and its next scheduled fire time will be adjusted to the next valid time after the misfire threshold.
```

**Q Could we delete the trigger to not run after certain misfire?  ** 
> Property: **.withMisfireHandlingInstructionDeleteData()**
> Property: **.withMisfireHandlingInstructionNowWithRemainingCount()**
```
// Create a CronTrigger with MISFIRE_INSTRUCTION_DELETE_DATA misfire instruction
CronTrigger trigger = TriggerBuilder.newTrigger()
 .withIdentity("yourTriggerName", "yourTriggerGroup")
 .withSchedule(CronScheduleBuilder.cronSchedule("yourCronExpression"))
 .withMisfireHandlingInstructionDeleteData() // Set misfire instruction
 .build();
 
 // Create a CronTrigger with MISFIRE_INSTRUCTION_NOW_WITH_REMAINING_REPEAT_COUNT misfire instruction
 CronTrigger trigger = TriggerBuilder.newTrigger()
  .withIdentity("yourTriggerName", "yourTriggerGroup")
  .withSchedule(CronScheduleBuilder.cronSchedule("yourCronExpression"))
  .withMisfireHandlingInstructionNowWithRemainingCount(3) // Set the number of retries
  .build(); 
```

**Q.** Explain  the state transitions in the QRTZ_FIRED_TRIGGERS table
```
In Quartz Scheduler, the QRTZ_FIRED_TRIGGERS table is used to track triggers that have been fired and are currently being executed or have been misfired. The table includes a STATE column that represents the current state of the fired trigger. Here are the common state transitions in the QRTZ_FIRED_TRIGGERS table:

WAITING:
The initial state when a trigger is created or scheduled but has not been fired yet.
Indicates that the trigger is waiting for its scheduled time to be reached.

ACQUIRED:
The trigger has been successfully acquired by a scheduler instance and is about to be executed.
This state signifies that the scheduler has determined it can fire the associated job.

EXECUTING:
The trigger is in the process of being executed.
Indicates that the associated job is currently being executed.
COMPLETE:

The trigger has been successfully executed.
This state indicates that the job associated with the trigger has completed its execution without errors.

ERROR:
The trigger execution has encountered an error.
This state is set when an exception occurs during the execution of the associated job.

BLOCKED:
The trigger is blocked from firing.
This state is typically used in clustered environments to prevent multiple schedulers from concurrently executing the same job.

PAUSED:
The trigger is in a paused state.
Paused triggers will not fire until they are resumed.

PAUSED_BLOCKED:
The trigger is both paused and blocked.
In a clustered environment, this state is used to prevent a paused trigger from firing until it is both resumed and unblocked.

MISFIRED:
The trigger has missed its scheduled firing time.
This state occurs when a trigger's scheduled time is in the past, but it wasn't able to be fired at that time. The scheduler will attempt to handle the misfire based on the trigger's misfire instruction.

MISFIRED_BLOCKED:
Similar to MISFIRED state, but the trigger is also blocked.

DELETED:
The trigger has been deleted.
Indicates that the trigger and associated firing data have been removed, likely due to manual intervention or the completion of the scheduled job.
Understanding these state transitions can be valuable for monitoring and troubleshooting job executions in Quartz Scheduler. It allows you to track the lifecycle of a trigger from its creation to execution and handle cases where triggers encounter errors or misfire.

```


