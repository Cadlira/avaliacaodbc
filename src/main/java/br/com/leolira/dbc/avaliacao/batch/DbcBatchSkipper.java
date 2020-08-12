package br.com.leolira.dbc.avaliacao.batch;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.exceptions.InputDataInvalidException;

@Component
public class DbcBatchSkipper implements SkipPolicy {

	private static final Log logger = LogFactory.getLog(DbcBatchSkipper.class);

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		if (isSkippableException(exception)) {
			getInvalidData(exception).ifPresent(invalidData -> {				
				logger.error(String.format("Dados invalidos, nao contabilizados no resultado final: %s",
						String.join(" - ", invalidData)));
			});
			return true;
		}
		return false;
	}

	private Optional<String[]> getInvalidData(Throwable exception) {
		if (isInputDataInvalidException(exception)) {
			return this.getInvalidDataFromInputDataInvalidException(exception);
		} else if (isCauseInputDataInvalidException(exception)){
			return this.getInvalidDataFromInputDataInvalidException(exception.getCause());
		}
		
		return Optional.empty();
	}
	
	private boolean isCauseInputDataInvalidException(Throwable exception) {
		return this.isInputDataInvalidException(exception.getCause());
	}
	
	private Optional<String[]> getInvalidDataFromInputDataInvalidException(Throwable exception) {
		return Optional.of(((InputDataInvalidException) exception).getInvalidData());
	}
	
	private boolean isSkippableException(Throwable exception) {
		return (this.isFlatFileParseExceptionValid(exception) || this.isInputDataInvalidException(exception));
	}
	
	private boolean isFlatFileParseExceptionValid(Throwable exception) {
		return ((exception instanceof FlatFileParseException) && this.isInputDataInvalidException(exception.getCause()));
	}

	private boolean isInputDataInvalidException(Throwable exception) {
		return (exception instanceof InputDataInvalidException);
	}

}
