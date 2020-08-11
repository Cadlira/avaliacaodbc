package br.com.leolira.dbc.avaliacao.readers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import br.com.leolira.dbc.avaliacao.factories.DbcDataFactory;
import br.com.leolira.dbc.avaliacao.models.DbcData;

public class DbcCustomDataMapper implements FieldSetMapper<DbcData>{

	@Override
	public DbcData mapFieldSet(FieldSet fieldSet) throws BindException {
		
		return DbcDataFactory.build(fieldSet.getValues());
	}

}
