/*
 * Created on 13-Feb-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import fg.eternity.util.LangUtils;
import fg.eternity.validator.Validator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanExecutor implements IPlan,IPlanChainable {

	@Setter @Getter
	private Executor executor;

	@Setter
	private IPlan first;

	@Getter @Setter
	private IPlan next;

	public IRun compile(IndexMap indexMap, int level) {
		Validator.notNull(executor,"executor cannot be null!!!");
		next.compile(indexMap,level);
		return () ->  {
			final byte[] serializedBoard = LangUtils.sneakyThrows(() -> {
				try(ByteArrayOutputStream os=new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(os)){
					objectOutputStream.writeObject(first);
					return os.toByteArray();
				}
			});

			IPlan iPlan = LangUtils.sneakyThrows(() -> {
				try(ByteArrayInputStream is=new ByteArrayInputStream(serializedBoard); ObjectInputStream objectInputStream = new ObjectInputStream(is);  ){
					return (IPlan)objectInputStream.readObject();
				}
			});

			for(;iPlan!=null && !(iPlan instanceof PlanExecutor);iPlan = ((IPlanChained)iPlan).getNext());

			Validator.notNull(iPlan,"iPlan cannot be null!!!");
			Validator.isTrue(iPlan instanceof PlanExecutor,"chain must have PlanExecutor!!!");

			PlanExecutor planExecutor = (PlanExecutor) iPlan;

			final IRun run = planExecutor.next.compile(indexMap, level);

			executor.execute(run::call);


		};
	}


	public void stop() {
	}

}
