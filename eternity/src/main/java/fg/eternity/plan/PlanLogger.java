package fg.eternity.plan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fg.eternity.bo.Config;
import fg.eternity.bo.Field;
import fg.eternity.bo.GoalDTO;
import fg.eternity.util.LangUtils;
import fg.eternity.validator.Validator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Slf4j
public class PlanLogger implements IPlanChainable, ILabelled {

	@Setter @Getter
	private PlanSet planSet;

	private long[] oldTryCounts = new long[Config.MAXLEVEL];

	private long[] newTryCounts = new long[Config.MAXLEVEL];

	private long[] oldAllCounts = new long[Config.MAXLEVEL];

	private long[] newAllCounts = new long[Config.MAXLEVEL];

	private long time = 0;

	private long speed = 0;

	private long maxLevel = 0;

	private long absoluteLevel = 0;

	private long count = 0;

	@Setter @Getter
	private String loggerName;
	@Setter @Getter
	private IPlan next;
	@Setter @Getter
	private IPlan first;

	@Setter @Getter
	private boolean logToTargetFolder;
	@Setter @Getter
	private String targetFolder;


	protected void logPlan() {

		ILabelled[] labelleds = PlanHelper.getAllLabelled(first);
		String rootDescription = "";
		for (int i = 0; i < labelleds.length; i++) {
			ILabelled labelled = labelleds[i];
			rootDescription += rootDescription.length() == 0 ? "" : ",";
			rootDescription += labelled.getLabel();
			if (labelled instanceof IPlanSimple) {
				IPlanSimple planSimple = (IPlanSimple) labelled;
				long oldTryCount = oldTryCounts[planSimple.getLevel()];
				long newTryCount = newTryCounts[planSimple.getLevel()];
				long oldAllCount = oldAllCounts[planSimple.getLevel()];
				long newAllCount = newAllCounts[planSimple.getLevel()];
				long tryCount = newTryCount - oldTryCount;
				long allCount = newAllCount - oldAllCount;
				if (allCount > 0 || tryCount > 0) {
					rootDescription += "=>" + allCount + "/" + tryCount;
				}
			}
		}

		log.info("{} Branch result:{}" ,loggerName, result());
		log.info("{} {}",loggerName, rootDescription);
		log.info("{} {}",loggerName,
				Field.toStringByOrder(first, planSet.getFieldOccupation()));

	}

	public IRun compile(IndexMap indexMap, int level) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		Validator.isTrue(!logToTargetFolder||targetFolder!=null,"Target folder is mandatory");
		final IRun run = next.compile(indexMap, level);
		return () ->  {
				Arrays.fill(oldAllCounts, 0);
				Arrays.fill(oldTryCounts, 0);
				PlanHelper.saveAllCounts(first, oldAllCounts);
				PlanHelper.saveTryCounts(first, oldTryCounts);

				long oldAllCount = 0;
				long oldTryCount = 0;
				for (int i = 0; i < oldTryCounts.length; i++) {
					oldTryCount += oldTryCounts[i];
					oldAllCount += oldAllCounts[i];
				}
				String boardTxt = Field.toString(planSet.getFields(), planSet
					.getFieldOccupation());
				log.info(
						"{} [{}]/[{}] branch started =>{}",loggerName,oldAllCount,oldTryCount,boardTxt);

				long lastTime = System.currentTimeMillis();

				run.call();

				long newTime = System.currentTimeMillis();

				Arrays.fill(newAllCounts, 0);
				Arrays.fill(newTryCounts, 0);
				PlanHelper.saveAllCounts(first, newAllCounts);
				PlanHelper.saveTryCounts(first, newTryCounts);

				long newAllCount = 0;
				long newTryCount = 0;
				for (int i = 0; i < newTryCounts.length; i++) {
					newTryCount += newTryCounts[i];
					newAllCount += newAllCounts[i];
				}
				log.info(
						"{} [{}/{}] branch ended",loggerName,newAllCount,newTryCount);

				//Speed
				try {
					time = newTime - lastTime;
					count = newAllCount - oldAllCount;
					speed = (newAllCount - oldAllCount) * 1000 / time;
				} catch (ArithmeticException e) {
					speed = -1;
				}

				//Counts
				int maxLev;
				for (maxLev = newAllCounts.length - 1; maxLev >= 0
						&& newAllCounts[maxLev] == oldAllCounts[maxLev]; maxLev--)
					;
				maxLevel = maxLev;
				for (maxLev = newAllCounts.length - 1; maxLev >= 0
						&& newAllCounts[maxLev] == 0; maxLev--)
					;
				absoluteLevel = maxLev;

				logPlan();


			if(logToTargetFolder){
				GoalDTO goalDTO =new GoalDTO();
				goalDTO.setBoardTxt(boardTxt);
				goalDTO.setName(loggerName);
				goalDTO.setAbsoluteLevel(absoluteLevel);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

				File file = new File(new File(targetFolder),"data"+dateFormat.format(new Date())+".json");
				LangUtils.sneakyThrows(() -> {
					try(FileOutputStream fo= new FileOutputStream(file)){
						fo.write(objectMapper.writeValueAsString(goalDTO).getBytes());
					}
				});
			}

		};
	}

	private String result() {
		return "Max Level:" + maxLevel + " Absolute Level:" + absoluteLevel
				+ " Time:" + time + " Speed:" + speed + " Count:" + count;
	}

	public long getPlanCounter() {
		return 0;
	}

	public long getTryCounter() {
		return 0;
	}

	public Field[] getFields() {
		return Field.NULLARRAY;
	}

	public void stop() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ILabelled#getLabel()
	 */
	public String getLabel() {
		return loggerName;
	}


}