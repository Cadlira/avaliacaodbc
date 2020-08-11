package br.com.leolira.dbc.avaliacao.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.leolira.dbc.avaliacao.factories.DbcDataFactory;

public class DbcDataTest {
	private static final String SALESMAN_1 = "001ç1234567891234çPedroç50000"; 
	private static final String CLIENT_1 = "002ç2345675434544345çJose da SilvaçRural"; 
	private static final String SALES_1 = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
	
	private static final String SALESMAN_2 = "001ç123456789çJoaoç50000"; 
	private static final String CLIENT_2 = "002ç1234578798çPadaria 1çUrbano"; 
	private static final String SALES_2 = "003ç11ç[1-5-100,2-30-2.50,3-1000-2.10]çJoao";
	
	@Test
	public void testCalcTotalOfDbcDataSales() {
		DbcDataSales sale = (DbcDataSales) DbcDataFactory.build(SALES_1.split("ç"));
		assertEquals(1199, sale.getTotal());
	}
	
	@Test
	public void testCalcSales1LowerThanSales2() {
		DbcDataSales sale1 = (DbcDataSales) DbcDataFactory.build(SALES_1.split("ç"));
		DbcDataSales sale2 = (DbcDataSales) DbcDataFactory.build(SALES_2.split("ç"));
		assertEquals(1199, sale1.getTotal());
		assertEquals(2675, sale2.getTotal());
		assertTrue(sale1.getTotal() < sale2.getTotal());
	}
	
	@Test
	public void testDbcDataResultWith2SalesmanAnd2SalesAnd2Clients() {
		List<DbcData> datas = new ArrayList<DbcData>();
		
		datas.add(DbcDataFactory.build(SALESMAN_1.split("ç")));
		datas.add(DbcDataFactory.build(CLIENT_1.split("ç")));
		datas.add(DbcDataFactory.build(SALES_1.split("ç")));
		datas.add(DbcDataFactory.build(SALESMAN_2.split("ç")));
		datas.add(DbcDataFactory.build(CLIENT_2.split("ç")));
		datas.add(DbcDataFactory.build(SALES_2.split("ç")));
		
		DbcDataResult result = new DbcDataResult(datas);
		assertEquals("Pedro", result.getWorstSalesmanName());
		assertEquals(2, result.getClientQuantity());
		assertEquals(2, result.getSalesmanQuantity());
		assertEquals("11", result.getMoreExpensiveSalesId());
		
	}
}
