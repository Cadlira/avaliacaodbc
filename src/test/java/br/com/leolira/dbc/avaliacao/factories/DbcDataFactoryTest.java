package br.com.leolira.dbc.avaliacao.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.models.DbcDataClient;
import br.com.leolira.dbc.avaliacao.models.DbcDataSales;
import br.com.leolira.dbc.avaliacao.models.DbcDataSalesman;
import br.com.leolira.dbc.avaliacao.models.DbcDataType;

public class DbcDataFactoryTest {

	
	private static final String SALESMAN_LINE = "001ç1111111111çVendedor testeç2000";
	private static final String SALESMAN_INCOMPLETE_LINE = "001ç1111111111çVendedor teste";
	private static final String SALESMAN_EXTRA_LINE = "001ç1111111111çVendedor testeç2000ç45454ç54545ç45454";
	
	private static final String CLIENT_LINE = "002ç2222222222çCliente testeçRURAL";
	private static final String CLIENT_INCOMPLETE_LINE = "002ç2222222222çCliente teste";
	private static final String CLIENT_EXTRA_LINE = "002ç2222222222çCliente testeçRURALç45454ç54545ç45454";

	private static final String SALES_LINE = "003ç10ç[1-10-20,2-10-35.5]çVendedor Test";
	private static final String SALES_INCOMPLETE_LINE = "003ç10ç[1-10-20,2-10-35.5]";
	private static final String SALES_EXTRA_LINE = "003ç10ç[1-10-20,2-10-35.5]çVendedor Testç45454ç54545ç45454";
	
	private static final String UNKOWN_TYPE_LINE = "004ç10ç[1-10-20,2-10-35.5]çVendedor Testç45454ç54545ç45454";
	
	@Test
	public void givenCompleteDataOfTypeSalesman() {
		DbcData data = DbcDataFactory.build(SALESMAN_LINE.split("ç"));
		assertEquals((DbcDataType.SALESMAN), data.getType());
		assertEquals("Vendedor teste", ((DbcDataSalesman)data).getName());
		assertEquals("1111111111", ((DbcDataSalesman)data).getDocument());
		assertEquals(2000, ((DbcDataSalesman)data).getSalary());
	}	
	@Test
	public void givenIncompleteDataOfTypeSalesman() {
		DbcData data = DbcDataFactory.build(SALESMAN_INCOMPLETE_LINE.split("ç"));
		assertEquals((DbcDataType.UNDEFINED), data.getType());
	}
	@Test
	public void givenExtraDataOfTypeSalesman() {
		DbcData data = DbcDataFactory.build(SALESMAN_EXTRA_LINE.split("ç"));
		assertEquals((DbcDataType.SALESMAN), data.getType());
		assertEquals("Vendedor teste", ((DbcDataSalesman)data).getName());
		assertEquals("1111111111", ((DbcDataSalesman)data).getDocument());
		assertEquals(2000, ((DbcDataSalesman)data).getSalary());
	}
	
	@Test
	public void givenCompleteDataOfTypeClient() {
		DbcData data = DbcDataFactory.build(CLIENT_LINE.split("ç"));
		assertEquals((DbcDataType.CLIENT), data.getType());
		assertEquals("Cliente teste", ((DbcDataClient)data).getName());
		assertEquals("2222222222", ((DbcDataClient)data).getDocument());
		assertEquals("RURAL", ((DbcDataClient)data).getBusinessArea());
	}	
	@Test
	public void givenIncompleteDataOfTypeClient() {
		DbcData data = DbcDataFactory.build(CLIENT_INCOMPLETE_LINE.split("ç"));
		assertEquals((DbcDataType.UNDEFINED), data.getType());
	}
	@Test
	public void givenExtraDataOfTypeClient() {
		DbcData data = DbcDataFactory.build(CLIENT_EXTRA_LINE.split("ç"));
		assertEquals((DbcDataType.CLIENT), data.getType());
		assertEquals("Cliente teste", ((DbcDataClient)data).getName());
		assertEquals("2222222222", ((DbcDataClient)data).getDocument());
		assertEquals("RURAL", ((DbcDataClient)data).getBusinessArea());
	}
	
	@Test
	public void givenCompleteDataOfTypeSales() {
		DbcData data = DbcDataFactory.build(SALES_LINE.split("ç"));
		assertEquals((DbcDataType.SALES), data.getType());
		assertEquals("10", ((DbcDataSales)data).getId());
		assertEquals(2, ((DbcDataSales)data).getItens().size());
		assertEquals("Vendedor Test", ((DbcDataSales)data).getSalesmanName());
		assertEquals(55.5, ((DbcDataSales)data).getTotal());
	}	
	@Test
	public void givenIncompleteDataOfTypeSales() {
		DbcData data = DbcDataFactory.build(SALES_INCOMPLETE_LINE.split("ç"));
		assertEquals((DbcDataType.UNDEFINED), data.getType());
	}
	@Test
	public void givenExtraDataOfTypeSalas() {
		DbcData data = DbcDataFactory.build(SALES_EXTRA_LINE.split("ç"));
		assertEquals((DbcDataType.SALES), data.getType());
		assertEquals("10", ((DbcDataSales)data).getId());
		assertEquals(2, ((DbcDataSales)data).getItens().size());
		assertEquals("Vendedor Test", ((DbcDataSales)data).getSalesmanName());
		assertEquals(55.5, ((DbcDataSales)data).getTotal());
	}
	
	@Test
	public void givenUnknownType() {
		DbcData data = DbcDataFactory.build(UNKOWN_TYPE_LINE.split("ç"));
		assertEquals((DbcDataType.UNDEFINED), data.getType());
	}
}
