package cn.tcl.platform.importSo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.taglibs.standard.lang.jstl.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.importSo.service.ImportSoService;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.importSo.dao.ImportSoDao;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.sellIn.dao.ISellInDao;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;

@Service("importSoService")
public class ImportPHExcelServiceImpl extends BaseAction implements ImportSoService {
	private static final int roeExchange = 0;
	@Autowired
	private ImportSoDao importExcelDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;

	@Autowired
	private ISellInDao sellInDao;
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	@Override
	public String read2007ExcelByPh(File file, String what) throws IOException {
		String countrySale = "";
		long start = System.currentTimeMillis();
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(3);
			XSSFRow rowPrice = sheet.getRow(4);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS = new HashSet<String>();
			Set<String> setModelSTO = new HashSet<String>();
			String countryId = WebPageUtil.getLoginedUser().getPartyId();
			ArrayList arrayList = new ArrayList<>();
			for (int j = 26; j < ro.getLastCellNum(); j++) {
				cells = ro.getCell(j);

				HashMap<String, Object> hashMap = new HashMap<>();

				String values = "";
				XSSFRow whatRow = sheet.getRow(5);
				if (cells != null && cells.getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& getCellValueByCell(cells)!= null
				
				) {

					String li = "";
					String cond   = " AND t.`party_id`='"+countryId+"'";
					
					if (values.contains("SUB-TOTAL") || values.contains("TTL")) {
						arrayList.add(j);
						hashMap.put("model", j);
						hashMap.put("what", "sum");
					} else {

						int model = modelMapDao.getModelIdByParty(cond, getCellValueByCell(cells), li);
						if (model >= 1) {
							hashMap.put("model",  getCellValueByCell(cells));
							// 判断为so or stock or sample
							if (whatRow.getCell(j) != null
									&& whatRow.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
									&& !getCellValueByCell(whatRow.getCell(j)).equals("")) {
								if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")) {
									hashMap.put("what", "Sold");
									if (rowPrice != null && rowPrice.getCell(j) != null
											&& rowPrice.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
											&& !rowPrice.getCell(j).equals("")) {

										switch (rowPrice.getCell(j).getCellType()) {

										case HSSFCell.CELL_TYPE_STRING:

											msg.append(getText("excel.error.row") + (5)+" " + getText("excel.error.cell")
													+ DateUtil.getExcelColumnLabel(j + 1) + " "+ getText("excel.error.num")
													+ "<br/>");

											break;

										case HSSFCell.CELL_TYPE_FORMULA:
											

											msg.append(getText("excel.error.row") + (5) +" "+ getText("excel.error.cell")
													+ DateUtil.getExcelColumnLabel(j + 1)+ " " + getText("excel.error.num")
													+ "<br/>");

											break;

										case HSSFCell.CELL_TYPE_NUMERIC:

												hashMap.put("price", rowPrice.getCell(j).getNumericCellValue());

											break;

										case HSSFCell.CELL_TYPE_ERROR:

											break;

										}

									} else {
										if (countryId != null && !countryId.equals("")) {

											Float price = importExcelDao.selectPrice(countryId,
													getCellValueByCell(cells));
											if (price != null && !price.equals("") && price != 0 && price != 0.0) {

												hashMap.put("price", price);

											} else {
												hashMap.put("price", 0);
												// msg.append(getText("excel.error.line") +
												// DateUtil.getExcelColumnLabel(j +1)+" "+cells.getStringCellValue()+" "
												// + getText("excel.error.price") + "<br/>");

											}

										}

									}
								} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase()
										.equals("stocks")) {
									hashMap.put("what", "Stocks");
									boolean one = setModelSTO.add(getCellValueByCell(cells));
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)
												+ " " + getText("excel.error.stoRE") + "  ("
												+ getCellValueByCell(cells) + ") " + "<br/>");
									}

								} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase()
										.equals("display")) {
									hashMap.put("what", "DisPlay");
									boolean one = setModelDIS.add(getCellValueByCell(cells));
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)
												+ " " + getText("excel.error.disRE") + "  ("
												+ getCellValueByCell(cells) + ") " + "<br/>");
									}

								} else {
									msg.append(getText("excel.error.row")  + 6 + " " + getText("excel.error.line")
											+ DateUtil.getExcelColumnLabel(j + 1) + " " + "("
											+ getCellValueByCell(cells) + ")" + getText("excel.error.what") + "<br/>");
								}

							}

						} else {
							msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
									+ getText("excel.error.model") + "(" + getCellValueByCell(cells) + ")" + "<br/>");

						}

					}

				} else {
					arrayList.add(j);
					hashMap.put("model", j);
					hashMap.put("what", "sum");
				}
				modelList.add(hashMap);
			}
			System.out.println(modelList);
			System.out.println(arrayList);
			System.out.println(modelList.size());

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 8; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();

				String shopName = "";
				Excel shop = null;
				if (row.getCell(4) != null && row.getCell(4).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& ! getCellValueByCell(row.getCell(4)).equals("")) {
					shopName = getCellValueByCell(row.getCell(4));
					shop = importExcelDao.getShopByNames(shopName);
					if (shop == null) {
						msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.shop") + "("
								+ getCellValueByCell(row.getCell(4))+ ")" + "<br/>");
					}

				} else {
					if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
							&& ! getCellValueByCell(row.getCell(5)).equals("")) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(5) + " " + getText("excel.error.null") + "<br/>");
					}

				}

				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(4) != null && row.getCell(4).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& !getCellValueByCell(row.getCell(4)).equals("")) {

					if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
							&& !getCellValueByCell(row.getCell(5)).equals("")) {
						try {
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(5)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							if (dt1.getTime() < dt2.getTime()) {
								msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(5 + 1)+" " + getText("excel.error.time") + "<br/>");
							} else {
								rowDate = newD;
								

							}
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(5 + 1) +" "+ getText("excel.error.date") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(5 + 1) +" "+ getText("excel.error.dateNo") + "<br/>");
					}

				}

				for (int m = 0; m < modelList.size(); m++) {
					int qtyCell = 26 + m;
					if (modelList.get(m).get("what") != null && !modelList.get(m).get("what").toString().equals("")
							&& (!modelList.get(m).get("what").toString().equals("sum")
									&& !modelList.get(m).get("model").toString().equals(qtyCell + ""))

					) {

						if (row.getCell(qtyCell) != null
								&& row.getCell(qtyCell).getCellType() != HSSFCell.CELL_TYPE_BLANK

						) {
							switch (row.getCell(qtyCell).getCellType()) {

							case HSSFCell.CELL_TYPE_STRING:
								msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell + 1)+" " + getText("excel.error.num")
										+ "<br/>");
								break;

							case HSSFCell.CELL_TYPE_FORMULA:
								try {
								FormulaEvaluator evaluator = row.getCell(qtyCell).getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
					        	evaluator.evaluateFormulaCell(row.getCell(qtyCell));
					        	CellValue cellValues = evaluator.evaluate(row.getCell(qtyCell));
					        	double celldata = cellValues.getNumberValue();
					        	
								/*msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell+1)+ " " + getText("excel.error.num") + "<br/>");*/
					        	if (celldata != 0 && celldata!=0.0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelList.get(m).get("model"));
									if (countryId != null) {
										countrySale = countryId;
										modelMap.put("Country", countryId == null ? null : countryId);
									}

									if (shop != null) {
										modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
										modelMap.put("StoreName", shopName);
									}

									

									if (rowDate != null && !rowDate.equals("")) {

										modelMap.put("DataDate", rowDate);
										if (countryId != null && !countryId.equals("")
												&&
												modelList.get(m).get("price") != null
												&& !modelList.get(m).get("price").toString().equals("")
												&& Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
													String[] days = rowDate.split("-");
													String begin = days[0] + "-" + days[1] + "-01";
													String end = days[0] + "-" + days[1] + "-31";
													Float exchange = importExcelDao.selectExchange(countryId, begin, end, rowDate);
													if (exchange == null) {
														msg.append(getText("excel.error.exchange") + " " + days[1] + "/" + days[0]
																+ "<br/>");
													} else {
														modelMap.put("exchange", exchange);
													}

												}

									}

								/*	if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
										modelMap.put("exchange", rowExchange);
									}*/
									if (modelList.get(m).get("price") != null
											&& !modelList.get(m).get("price").toString().equals("")) {
										modelMap.put("Price", modelList.get(m).get("price"));

									}
									if (modelList.get(m).get("what").toString().equals("Sold")) {
										modelMap.put("sold", (int)celldata);
									} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

										modelMap.put("display", (int)celldata);
									} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

										modelMap.put("stocks", (int)celldata);
									}

									allModelList.add(modelMap);

								}
								/*msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell + 1) + getText("excel.error.num")
										+ "<br/>");*/
							}catch (Exception e) {
								msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(qtyCell + 1)+"  "+ getText("excel.error.formulaError") + "<br/>");
							}
								break;

							case HSSFCell.CELL_TYPE_NUMERIC:

								if (row.getCell(qtyCell).getNumericCellValue() != 0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelList.get(m).get("model"));
									if (countryId != null) {
										countrySale = countryId;
										modelMap.put("Country", countryId == null ? null : countryId);
									}

									if (shop != null) {
										modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
										modelMap.put("StoreName", shopName);
									}

									if (rowDate != null && !rowDate.equals("")) {

										modelMap.put("DataDate", rowDate);

									}

									if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
										modelMap.put("exchange", rowExchange);
									}
									if (modelList.get(m).get("price") != null
											&& !modelList.get(m).get("price").toString().equals("")) {
										modelMap.put("Price", modelList.get(m).get("price"));

									}
									if (modelList.get(m).get("what").toString().equals("Sold")) {
										modelMap.put("sold", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

										modelMap.put("display", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

										modelMap.put("stocks", (int) row.getCell(qtyCell).getNumericCellValue());
									}

									allModelList.add(modelMap);

								}

								break;

							case HSSFCell.CELL_TYPE_ERROR:

								break;

							}

						}

					} else {
						// ++qtyCell;
					}

				}

				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

					if (j >= 0) {
						cell = row.getCell(j);
					}

					if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}
			String userId = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				userId = (WebPageUtil.getLoginedUserId());
			}
			List<ImportExcel> excelList = new ArrayList<ImportExcel>();
			List<ImportExcel> stockList = new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe = new ArrayList<ImportExcel>();

			List<ImportExcel> displayListDelete = new ArrayList<ImportExcel>();
			List<ImportExcel> stocksListByDelete = new ArrayList<ImportExcel>();
			System.out.println("=========allModelList===========" + allModelList);
			if (msg.length() <= 0) {
				for (int i = 0; i < allModelList.size(); i++) {

					if (

					allModelList.get(i).get("Store") != null && allModelList.get(i).get("Store") != ""

							&& allModelList.get(i).get("Model") != null && allModelList.get(i).get("Model") != ""

							&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""

							&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
							&& userId != null && userId != ""

					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate").toString());
						excel.setShopId(Integer.parseInt(allModelList.get(i).get("Store").toString()));

						excel.setCountryId(allModelList.get(i).get("Country").toString());

						if (allModelList.get(i).get("sold") != null
								&& Integer.parseInt(allModelList.get(i).get("sold").toString()) != 0
								&& allModelList.get(i).get("Price") != null) {

							BigDecimal bd = new BigDecimal(allModelList.get(i).get("sold").toString());

							int qty = bd.intValue();
							excel.setSaleQty(bd.intValue());
							excel.setSource("PC");

							java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
							nf.setGroupingUsed(false);

							excel.setSalePrice(Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString()))));

							double price = Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString())));

							bd = new BigDecimal(qty * price);

							excel.setAmt(String.valueOf(bd.longValue()));
							excel.setH_qty(qty);
							if (allModelList.get(i).get("exchange") != null
									&& Float.parseFloat(allModelList.get(i).get("exchange").toString()) != 0) {

								Float exchange = Float.parseFloat(allModelList.get(i).get("exchange").toString());
								
								if (exchange != null) {
									bd = new BigDecimal(price * exchange);
									double Hprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									excel.setH_price(Hprice);
									bd = new BigDecimal(qty * Hprice);
									BigDecimal HAmt = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
									excel.setH_amt(String.valueOf(HAmt));
								}

							}else {
								excel.setH_price(0);
								excel.setH_amt(String.valueOf('0'));
							}

							excelList.add(excel);

						} else if (allModelList.get(i).get("stocks") != null
								&& Integer.parseInt(allModelList.get(i).get("stocks").toString()) != 0) {
							excel.setStockQty(Integer.parseInt(allModelList.get(i).get("stocks").toString()));

							stocksListByDelete.add(excel);

							excel.setClassId(1);
							stockList.add(excel);

						} else if (allModelList.get(i).get("display") != null
								&& Integer.parseInt(allModelList.get(i).get("display").toString()) != 0) {
							excel.setDisPlayQty(Integer.parseInt(allModelList.get(i).get("display").toString()));

							displayListDelete.add(excel);

							excel.setClassId(1);
							displayListRe.add(excel);

						}

					}
				}

				// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}

			if (stocksListByDelete.size() > 0) {
				importExcelDao.deleteStockCountByRE(stocksListByDelete);
				if (stockList.size() > 0) {
					DateUtil.remove(stockList);

					importExcelDao.saveStocks(stockList);
				}
			}

			if (excelList.size() > 0) {
				int row = importExcelDao.saveSales(excelList);
				if (row > 0) {
					DateUtil.ListSort(excelList);
					String[] beginDate = excelList.get(0).getDataDate().split("-");
					String[] endDate = excelList.get(excelList.size() - 1).getDataDate().split("-");

					String beg = beginDate[0] + "-" + beginDate[1] + "-01";
					String end = endDate[0] + "-" + endDate[1] + "-31";
					String countryList = "";

					if (what.equals("shop")) {
						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
						importExcelDao.updateStocksByShop(excelList);
					}

				}
			}

			if (displayListDelete.size() > 0) {
				importExcelDao.deleteStockCountByRE(displayListDelete);
				if (displayListRe.size() > 0) {
					DateUtil.remove(displayListRe);

					importExcelDao.saveDisPlaysByRe(displayListRe);
				}
			}

			System.out.println("=========================Time" + (System.currentTimeMillis() - start));

			if (msg.length() > 0) {
				return msg.toString();
			} else {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}

	}

	@Override
	public String readExcel(File file, String fileName, String what) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			if (what.equals("country")) {
				return read2007ExcelByCountry(file, what);
			} else {
				if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 16) {
					return read2007ExcelByPK(file);

				} else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 19) {
					return read2007ExcelByVn(file, what);

				} else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 5) {
					return read2007ExcelByPh(file, what);

				} else {
					return null;
				}
			}

		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	@Override
	public void deleteCore(String countryId) throws Exception {
		importExcelDao.deleteCore(countryId);
	}

	@Override
	public void insertCore(String countryId, String line) throws Exception {
		importExcelDao.insertCore(countryId, line);
	}

	@Override
	public List<HashMap<String, Object>> selectCore(String countryId) throws Exception {
		return importExcelDao.selectCore(countryId);
	}

	@Override
	public XSSFWorkbook exporExcel(String spec, String conditions, String[] excelHeader, String title)
			throws Exception {
		String key = null;
		List<Shop> list = shopDao.selectShopName(conditions, null);

		List<HashMap<String, Object>> modelList = importExcelDao.selectModel(null, conditions, spec);
		// 设置 表头宽度
		int[] excelWidth = { 120, 180, 200, 120, 100, 100, 100, 100 };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		sheet.createFreezePane(4, 3, 4, 3);
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));

		// 导出字体样式
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12); // 字体大小
		font.setFontName("Times New Roman");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		XSSFFont fontNo = workbook.createFont();
		fontNo.setFontHeightInPoints((short) 12); // 字体大小
		fontNo.setFontName("Times New Roman");

		XSSFCellStyle styleNo = workbook.createCellStyle();
		styleNo.setFont(fontNo);

		// 导出样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(font);

		// 导出样式
		XSSFCellStyle styleBlue = workbook.createCellStyle();
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleBlue.setBottomBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleBlue.setLeftBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleBlue.setRightBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleBlue.setTopBorderColor(HSSFColor.BLACK.index);
		styleBlue.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBlue.setFont(font);

		XSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
		styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleYellow.setFont(font);

		XSSFCellStyle styleGreen = workbook.createCellStyle();
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
		styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleGreen.setFont(font);

		// 导出字体样式
		XSSFFont fontTwo = workbook.createFont();
		fontTwo.setFontHeightInPoints((short) 12); // 字体大小
		fontTwo.setFontName("Times New Roman");

		// 导出样式
		XSSFCellStyle styleTwo = workbook.createCellStyle();
		styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		styleTwo.setFont(fontTwo);

		XSSFRow rowNotice = sheetNotice.createRow(1);
		XSSFCell cellNotice = rowNotice.createCell(3);
		cellNotice.setCellValue("Notice:");
		cellNotice.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(2);
		XSSFCell cellNoticeTW = rowNotice.createCell(3);
		cellNoticeTW.setCellValue("1. Please fill in the chart with the font Times New Roman in size 12.");
		cellNoticeTW.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(3);
		XSSFCell cellNoticeTh = rowNotice.createCell(3);
		cellNoticeTh.setCellValue("1. 请将字体统一为Times New Roman，字体大小12号。");
		cellNoticeTh.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(4);
		XSSFCell cellNoticeFO = rowNotice.createCell(3);
		cellNoticeFO.setCellValue("2. Store name here must be exactly the same as the ones filled in shopsInfo.");
		cellNoticeFO.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(5);
		XSSFCell cellNoticeFI = rowNotice.createCell(3);
		cellNoticeFI.setCellValue("2. 门店名称必须与门店信息表中所填的名称完全一致。");
		cellNoticeFI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(6);
		XSSFCell cellNoticeSI = rowNotice.createCell(3);
		cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
		cellNoticeSI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(7);
		XSSFCell cellNoticeSE = rowNotice.createCell(3);
		cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
		cellNoticeSE.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(8);
		XSSFCell cellNoticeEI = rowNotice.createCell(3);
		cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
		cellNoticeEI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(9);
		XSSFCell cellNoticeNI = rowNotice.createCell(3);
		cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
		cellNoticeNI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(10);
		XSSFCell cellNoticeTe = rowNotice.createCell(3);
		cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
		cellNoticeTe.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(11);
		XSSFCell cellNoticeEL = rowNotice.createCell(3);
		cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
		cellNoticeEL.setCellStyle(styleNo);
		// --------------------------------------------------------------
		for (int i = 0; i < excelWidth.length; i++) {
			sheet.setColumnWidth(i, 32 * excelWidth[i]);
		}

		XSSFRow row = sheet.createRow(0);
		// 表头数据
		for (int i = 0; i < excelHeader.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);

		}
		XSSFRow rowTwo = sheet.createRow(1);
		XSSFRow rowThree = sheet.createRow(2);

		for (int i = 0; i < modelList.size(); i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleBlue);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleBlue);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i);
			cellOne.setCellValue("Sold");
			cellOne.setCellStyle(styleBlue);

		}
		int size;
		if (modelList.size() <= 2) {
			size = modelList.size();
		} else {
			size = 2;
		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i + modelList.size());
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleGreen);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i + modelList.size());
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleGreen);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i + modelList.size());
			cellOne.setCellValue("Display");
			cellOne.setCellStyle(styleGreen);

		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i + modelList.size() + size);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleYellow);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i + modelList.size() + size);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleYellow);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i + modelList.size() + size);
			cellOne.setCellValue("Stocks");
			cellOne.setCellStyle(styleYellow);

		}

		// 表体数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 3);
			Shop shop = list.get(i);

			// -------------单元格-------------------

			/**
			 * 国家
			 */
			if (i == 0) {
				XSSFCell cell = row.createCell(3);
				cell.setCellValue("dd/MM/yyyy");
				cell.setCellStyle(styleTwo);
			}

			if (shop.getCountryName() != null && !shop.getCountryName().equals("")) {
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(shop.getCountryName());
				cell0.setCellStyle(styleTwo);
			}

			if (shop.getShopName() != null && !shop.getShopName().equals("")) {
				XSSFCell cell0 = row.createCell(2);
				cell0.setCellValue(shop.getShopName());
				cell0.setCellStyle(styleTwo);
			}

			if (shop.getPartyName() != null && !shop.getPartyName().equals("")) {
				XSSFCell cell0 = row.createCell(1);
				cell0.setCellValue(shop.getPartyName());
				cell0.setCellStyle(styleTwo);
			}

		}

		return workbook;
	}

	@Override
	public XSSFWorkbook exporExcelByPK(String spec, String conditions, String[] excelHeader, String title)
			throws Exception {
		String key = null;
		List<Shop> list = shopDao.selectShopName(conditions, null);

		List<HashMap<String, Object>> modelList = importExcelDao.selectModel(null, conditions, spec);
		// 设置 表头宽度
		int[] excelWidth = { 120, 180, 200, 120, 100, 100, 100, 100 };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		sheet.createFreezePane(4, 3, 4, 3);
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 4, 4));

		// 导出字体样式
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12); // 字体大小
		font.setFontName("Times New Roman");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		XSSFFont fontNo = workbook.createFont();
		fontNo.setFontHeightInPoints((short) 12); // 字体大小
		fontNo.setFontName("Times New Roman");

		XSSFCellStyle styleNo = workbook.createCellStyle();
		styleNo.setFont(fontNo);

		// 导出样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(font);

		// 导出样式
		XSSFCellStyle styleBlue = workbook.createCellStyle();
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleBlue.setBottomBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleBlue.setLeftBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleBlue.setRightBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleBlue.setTopBorderColor(HSSFColor.BLACK.index);
		styleBlue.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBlue.setFont(font);

		XSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
		styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleYellow.setFont(font);

		XSSFCellStyle styleGreen = workbook.createCellStyle();
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
		styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleGreen.setFont(font);

		// 导出字体样式
		XSSFFont fontTwo = workbook.createFont();
		fontTwo.setFontHeightInPoints((short) 12); // 字体大小
		fontTwo.setFontName("Times New Roman");

		// 导出样式
		XSSFCellStyle styleTwo = workbook.createCellStyle();
		styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		styleTwo.setFont(fontTwo);

		XSSFRow rowNotice = sheetNotice.createRow(1);
		XSSFCell cellNotice = rowNotice.createCell(3);
		cellNotice.setCellValue("Notice:");
		cellNotice.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(2);
		XSSFCell cellNoticeTW = rowNotice.createCell(3);
		cellNoticeTW.setCellValue("1. Please fill in the chart with the font Times New Roman in size 12.");
		cellNoticeTW.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(3);
		XSSFCell cellNoticeTh = rowNotice.createCell(3);
		cellNoticeTh.setCellValue("1. 请将字体统一为Times New Roman，字体大小12号。");
		cellNoticeTh.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(4);
		XSSFCell cellNoticeFO = rowNotice.createCell(3);
		cellNoticeFO.setCellValue("2. Store name here must be exactly the same as the ones filled in shopsInfo.");
		cellNoticeFO.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(5);
		XSSFCell cellNoticeFI = rowNotice.createCell(3);
		cellNoticeFI.setCellValue("2. 门店名称必须与门店信息表中所填的名称完全一致。");
		cellNoticeFI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(6);
		XSSFCell cellNoticeSI = rowNotice.createCell(3);
		cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
		cellNoticeSI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(7);
		XSSFCell cellNoticeSE = rowNotice.createCell(3);
		cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
		cellNoticeSE.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(8);
		XSSFCell cellNoticeEI = rowNotice.createCell(3);
		cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
		cellNoticeEI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(9);
		XSSFCell cellNoticeNI = rowNotice.createCell(3);
		cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
		cellNoticeNI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(10);
		XSSFCell cellNoticeTe = rowNotice.createCell(3);
		cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
		cellNoticeTe.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(11);
		XSSFCell cellNoticeEL = rowNotice.createCell(3);
		cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
		cellNoticeEL.setCellStyle(styleNo);
		// --------------------------------------------------------------
		for (int i = 0; i < excelWidth.length; i++) {
			sheet.setColumnWidth(i, 32 * excelWidth[i]);
		}

		XSSFRow row = sheet.createRow(0);
		// 表头数据
		for (int i = 0; i < excelHeader.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);

		}
		XSSFRow rowTwo = sheet.createRow(1);
		XSSFRow rowThree = sheet.createRow(2);

		for (int i = 0; i < modelList.size(); i++) {

			// 开始行，结束行，开始列，结束列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, i * 3 + 5, i * 3 + 7));

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(i * 3 + 5);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleBlue);

			}

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(i * 3 + 6);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleBlue);

			}
			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(i * 3 + 7);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleBlue);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(i * 3 + 5);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleYellow);
			} else {
				XSSFCell cell = rowTwo.createCell(i * 3 + 5);
				cell.setCellValue("price");
				cell.setCellStyle(styleYellow);
			}

			XSSFCell cellOne = rowThree.createCell(i * 3 + 5);
			cellOne.setCellValue("Sold");
			cellOne.setCellStyle(styleGreen);

			XSSFCell cellTwo = rowThree.createCell(i * 3 + 6);
			cellTwo.setCellValue("Stocks");
			cellTwo.setCellStyle(styleGreen);

			XSSFCell cellThree = rowThree.createCell(i * 3 + 7);
			cellThree.setCellValue("Display");
			cellThree.setCellStyle(styleGreen);

		}

		// 表体数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 3);
			Shop shop = list.get(i);

			// -------------单元格-------------------

			/**
			 * 国家
			 */
			if (i == 0) {
				XSSFCell cell = row.createCell(4);
				cell.setCellValue("dd/MM/yyyy");
				cell.setCellStyle(styleTwo);
			}

			if (shop.getCountryName() != null && !shop.getCountryName().equals("")) {
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(shop.getCountryName());
				cell0.setCellStyle(styleTwo);
			}

			if (shop.getPartyName() != null && !shop.getPartyName().equals("")) {
				XSSFCell cell0 = row.createCell(1);
				cell0.setCellValue(shop.getPartyName());
				cell0.setCellStyle(styleTwo);
			}

			if (shop.getCustomerId() != null && !shop.getCustomerId().equals("")) {
				XSSFCell cell0 = row.createCell(2);
				cell0.setCellValue(shop.getCustomerId());
				cell0.setCellStyle(styleTwo);
			}

			if (shop.getShopName() != null && !shop.getShopName().equals("")) {
				XSSFCell cell0 = row.createCell(3);
				cell0.setCellValue(shop.getShopName());
				cell0.setCellStyle(styleTwo);
			}

		}

		return workbook;
	}

	@Override
	public String read2007ExcelByPK(File file) throws IOException {
		System.out.println("=======read2007ExcelByPK========");
		String countrySale = "";
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(3);
		//	XSSFRow roc = sheet.getRow(3);
			XSSFCell cells = null;
			//XSSFRow rowPrice = sheet.getRow(1);
			XSSFRow whatRow = sheet.getRow(4);
			XSSFRow whatRowTwo = sheet.getRow(5);
			int productStrat=0;
			int productEnd=0;
			  for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			        CellRangeAddress region = sheet.getMergedRegion(i); // 
			        int colIndex = region.getFirstColumn();             // 合并区域首列位置
			        int rowNum =1;                     // 合并区域首行位置
			        if(getCellValueByCell(sheet.getRow(rowNum).getCell(colIndex)).toLowerCase().equals("product")) {
			        	productStrat= region.getFirstColumn();
			        	productEnd=region.getLastColumn();
			        
			        
			        }
			     //   System.out.println("第[" + i + "]个合并区域：" +  );
			    }
			  
			//  sourceSheet.getNumMergedRegions(); 
			//  得到某一个合并单元格 CellRangeAddress oldRange=sourceSheet.getMergedRegion(i); 
			////  起始行 oldRange.getFirstRow() ； 
			//  结束行oldRange.getLastRow() 
			//  起始列oldRange.getFirstColumn()
			//  结束列oldRange.getLastColumn()
			 

			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS = new HashSet<String>();
			Set<String> setModelSTO = new HashSet<String>();
			String countryId = WebPageUtil.getLoginedUser().getPartyId();
			ArrayList arrayList=new ArrayList<>();
			
			for (int j = productStrat; j <ro.getLastCellNum(); j++) {
				// CellRangeAddress region = sheet.getMergedRegion(j); // 
			     // int colIndex = region.getFirstColumn();             // 合并区域首列位置
			       //int rowNum =3;
			       
				HashMap<String, Object> hashMap = new HashMap<>();
				cells = ro.getCell(j);
				String modelVaule=getMergedRegionValue(sheet, 3, j);
				if (modelVaule!=null && 
						  !modelVaule.equals("")
						&& !modelVaule.toUpperCase().equals("SUB TOTAL")
						) {
					String li = "";
					String cond = " AND t.`party_id`='" + countryId + "'";
					String values=modelVaule;
					int model = modelMapDao.getModelIdByParty(cond, values, li);
					if (model >= 1) {
						hashMap.put("model", values);

						if (whatRow.getCell(j) != null && whatRow.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
								&& !whatRow.getCell(j).equals("")) {
							if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sell out")
									||
									getCellValueByCell(whatRowTwo.getCell(j)).trim().toLowerCase().equals("sell out")) {
								hashMap.put("what", "Sold");
								hashMap.put("price", 0);
								
							} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stock")
									|getCellValueByCell(whatRowTwo.getCell(j)).trim().toLowerCase().equals("stocks")
									) {
								hashMap.put("what", "Stocks");

								boolean one = setModelSTO.add(values);
								if (!one) {
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 3) + " "
											+ getText("excel.error.stoRE") + "  (" + values + ") "
											+ "<br/>");

								}

							}/* else if (whatRow.getCell(j).getStringCellValue().trim().toLowerCase().equals("display")) {
								hashMap.put("what", "DisPlay");
								boolean one = setModelDIS.add(values);
								if (!one) {
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 3) + " "
											+ getText("excel.error.disRE") + "  (" + values + ") "
											+ "<br/>");
								}

							}*/ else {
								hashMap.put("what", "other");
								/*msg.append(getText("excel.error.row") + " " + 3 + " " + getText("excel.error.line")
										+ DateUtil.getExcelColumnLabel(j + 1) + " " + "(" + values
										+ ")" + getText("excel.error.what") + "<br/>");*/
							}

						}
					}
					if (model == 0) {

						SellIn channelModel = sellInDao.selectCustomerModel(values, countryId, "");
						if (channelModel == null) {
							msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
									+ getText("excel.error.model") + "(" + values + ")" + "<br/>");
						} else {
							hashMap.put("model", channelModel.getModel());

							if (whatRow.getCell(j) != null
									&& whatRow.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
									&& !whatRow.getCell(j).equals("")) {
								if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sell out")
										||
										getCellValueByCell(whatRowTwo.getCell(j)).trim().toLowerCase().equals("sell out")) {
									hashMap.put("what", "Sold");
									hashMap.put("price", 0);
									
								} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase()
										.equals("stock")
										|| getCellValueByCell(whatRowTwo.getCell(j)).trim().toLowerCase()
										.equals("stock")
										) {
									hashMap.put("what", "Stocks");

									boolean one = setModelSTO.add(values);
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)
												+ " " + getText("excel.error.stoRE") + "  ("
												+ values + ") " + "<br/>");

									}

								}/* else if (whatRow.getCell(j).getStringCellValue().trim().toLowerCase()
										.equals("display")) {
									hashMap.put("what", "DisPlay");
									boolean one = setModelDIS.add(values);
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)
												+ " " + getText("excel.error.disRE") + "  ("
												+ values + ") " + "<br/>");
									}

								}*/ else {
									/*msg.append(getText("excel.error.row") + " " + 3 + " " + getText("excel.error.line")
											+ DateUtil.getExcelColumnLabel(j + 1) + " " + "("
											+ values + ")" + getText("excel.error.what") + "<br/>");
											*/
									hashMap.put("what", "other");
								}

							}

						}

					}

				
				}else {
					arrayList.add(j);
					hashMap.put("model", j);
					hashMap.put("what", "sum");
				}
				modelList.add(hashMap);
			}

			
			System.out.println("=========modeList========" + modelList);

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 8; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				// XSSFRow rowPrice = sheet.getRow(1);
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if(row==null ) {
					continue;
				}
				
				String shopName = "";
				Shop shop = null;
				if (row.getCell(3) != null && row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& !getCellValueByCell(row.getCell(3)).equals("")) {
					shopName = getCellValueByCell(row.getCell(3));
					shop = shopDao.getShopByNames(shopName);
					if (shop == null) {
						msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.shop") + "("
								+getCellValueByCell(row.getCell(3)) + ")" + "<br/>");
					}

				} else {
					if ((row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK)
						&&	!getCellValueByCell(row.getCell(0)).equals("")
							) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.null") + "<br/>");

					}
				}

				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(3) != null
						&& row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK
								&& !getCellValueByCell(row.getCell(3)).equals("")
						) {

					if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
							&&	!getCellValueByCell(row.getCell(0)).equals("")) {
						try {
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(0)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							if (dt1.getTime() < dt2.getTime()) {
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(1) + " "+ getText("excel.error.time") + "<br/>");
							} else {
								rowDate = newD;
							

							}
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.cell")+" "
									+ DateUtil.getExcelColumnLabel(1)+ " " + getText("excel.error.date") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.cell")+" "
								+ DateUtil.getExcelColumnLabel(1)+ " " + getText("excel.error.dateNo") + "<br/>");
					}

				}

				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
				for (int m = 0; m < modelList.size(); m++) {
					int qtyCell=productStrat+m;
					if (modelList.get(m).get("what") != null && !modelList.get(m).get("what").toString().equals("")
							&& !modelList.get(m).get("what").toString().equals("other")
							&& (!modelList.get(m).get("what").toString().equals("sum")
									&& !modelList.get(m).get("model").toString().equals(qtyCell + "") )
									) {

						if (row.getCell(qtyCell) != null && row.getCell(qtyCell).getCellType() != HSSFCell.CELL_TYPE_BLANK

						) {
							switch (row.getCell(qtyCell).getCellType()) {

							case HSSFCell.CELL_TYPE_STRING:
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell+1) + " "+ getText("excel.error.num") + "<br/>");
								break;

							case HSSFCell.CELL_TYPE_FORMULA:
								try {
								FormulaEvaluator evaluator = row.getCell(qtyCell).getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
					        	evaluator.evaluateFormulaCell(row.getCell(qtyCell));
					        	CellValue cellValues = evaluator.evaluate(row.getCell(qtyCell));
					        	double celldata = cellValues.getNumberValue();
					        	
								/*msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell+1)+ " " + getText("excel.error.num") + "<br/>");*/
					        	if (celldata != 0 && celldata!=0.0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelList.get(m).get("model"));
									if (countryId != null) {
										countrySale = countryId;
										modelMap.put("Country", countryId == null ? null : countryId);
									}

									if (shop != null) {
										modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
										modelMap.put("StoreName", shopName);
									}

									

									if (rowDate != null && !rowDate.equals("")) {

										modelMap.put("DataDate", rowDate);
										if (countryId != null && !countryId.equals("")
												&&
												modelList.get(m).get("price") != null
												&& !modelList.get(m).get("price").toString().equals("")
												&& Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
													String[] days = rowDate.split("-");
													String begin = days[0] + "-" + days[1] + "-01";
													String end = days[0] + "-" + days[1] + "-31";
													Float exchange = importExcelDao.selectExchange(countryId, begin, end, rowDate);
													if (exchange == null) {
														msg.append(getText("excel.error.exchange") + " " + days[1]
																+ "/" + days[0] + "<br/>");
													} else {
														rowExchange = exchange;
													}

												}
									}

									/*if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
										modelMap.put("exchange", rowExchange);
									}*/
									if (modelList.get(m).get("price") != null
											&& !modelList.get(m).get("price").toString().equals("")) {
										modelMap.put("Price", modelList.get(m).get("price"));

									}
									if (modelList.get(m).get("what").toString().equals("Sold")) {
										modelMap.put("sold", (int)celldata);
									} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

										modelMap.put("display", (int)celldata);
									} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

										modelMap.put("stocks", (int)celldata);
									}

									allModelList.add(modelMap);

								}
							}catch (Exception e) {
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(qtyCell+1) +"  "+ getText("excel.error.formulaError") + "<br/>");
							}
								break;

							case HSSFCell.CELL_TYPE_NUMERIC:

								if (row.getCell(qtyCell).getNumericCellValue() != 0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelList.get(m).get("model"));
									if (countryId != null) {
										countrySale = countryId;
										modelMap.put("Country", countryId == null ? null : countryId);
									}

									if (shop != null) {
										modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
										modelMap.put("StoreName", shopName);
									}

									

									if (rowDate != null && !rowDate.equals("")) {

										modelMap.put("DataDate", rowDate);

									}

									if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
										modelMap.put("exchange", rowExchange);
									}
									if (modelList.get(m).get("price") != null
											&& !modelList.get(m).get("price").toString().equals("")) {
										modelMap.put("Price", modelList.get(m).get("price"));

									}
									if (modelList.get(m).get("what").toString().equals("Sold")) {
										modelMap.put("sold", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

										modelMap.put("display", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

										modelMap.put("stocks", (int) row.getCell(qtyCell).getNumericCellValue());
									}

									allModelList.add(modelMap);

								}

								break;

							case HSSFCell.CELL_TYPE_ERROR:

								break;

							}

						}

					}

				}
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}

			System.out.println("=====size=============" + allModelList.size());
			System.out.println("=====allModelList=============" + allModelList);

			String userId = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				userId = WebPageUtil.getLoginedUserId();
			}
			List<ImportExcel> excelList = new ArrayList<ImportExcel>();
			List<ImportExcel> stockList = new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe = new ArrayList<ImportExcel>();

			List<ImportExcel> displayListDelete = new ArrayList<ImportExcel>();
			List<ImportExcel> stocksListDelete = new ArrayList<ImportExcel>();
			
			if (msg.length() <= 0) {
			
				for (int i = 0; i < allModelList.size(); i++) {

					int row = 0;
					if (

					allModelList.get(i).get("Store") != null && allModelList.get(i).get("Store") != ""

							&& allModelList.get(i).get("Model") != null && allModelList.get(i).get("Model") != ""
							&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""
							&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
							&& userId != null && userId != ""

					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate").toString());
						excel.setShopId(Integer.parseInt(allModelList.get(i).get("Store").toString()));
						excel.setCountryId(allModelList.get(i).get("Country").toString());

						if (allModelList.get(i).get("sold") != null
								&& Integer.parseInt(allModelList.get(i).get("sold").toString()) != 0
								&& allModelList.get(i).get("Price") != null ) {

							BigDecimal bd = new BigDecimal(allModelList.get(i).get("sold").toString());

							int qty = bd.intValue();
							excel.setSaleQty(bd.intValue());
							excel.setSource("PC");

							java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
							nf.setGroupingUsed(false);

							excel.setSalePrice(Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString()))));

							double price = Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString())));

							bd = new BigDecimal(qty * price);

							excel.setAmt(String.valueOf(bd.longValue()));
							excel.setH_qty(qty);
							if (allModelList.get(i).get("exchange") != null
									&& Float.parseFloat(allModelList.get(i).get("exchange").toString()) != 0) {

								Float exchange = Float.parseFloat(allModelList.get(i).get("exchange").toString());
								
								if (exchange != null) {
									bd = new BigDecimal(price * exchange);
									double Hprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									excel.setH_price(Hprice);
									bd = new BigDecimal(qty * Hprice);
									BigDecimal HAmt = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
									excel.setH_amt(String.valueOf(HAmt));
								}

							}else {
								excel.setH_price(0);
								excel.setH_amt(String.valueOf('0'));
							}

							excelList.add(excel);

						} else if (allModelList.get(i).get("stocks") != null
								&& Integer.parseInt(allModelList.get(i).get("stocks").toString()) != 0) {
							excel.setStockQty(Integer.parseInt(allModelList.get(i).get("stocks").toString()));


							stocksListDelete.add(excel);
							
							excel.setClassId(1);
							stockList.add(excel);
							

						} else if (allModelList.get(i).get("display") != null
								&& Integer.parseInt(allModelList.get(i).get("display").toString()) != 0) {
							excel.setDisPlayQty(Integer.parseInt(allModelList.get(i).get("display").toString()));
							displayListDelete.add(excel);
							excel.setClassId(1);
							displayListRe.add(excel);
							
							
						}

					}
				}

				// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			if (stocksListDelete.size() > 0) {
				importExcelDao.deleteStockCountByRE(stocksListDelete);
				if (stockList.size() > 0) {
					DateUtil.remove(stockList);

					importExcelDao.saveStocks(stockList);
				}
			}

			if (excelList.size() > 0) {
				int row = importExcelDao.saveSales(excelList);
				if (row > 0) {
					DateUtil.ListSort(excelList);
					String[] beginDate = excelList.get(0).getDataDate().split("-");
					String[] endDate = excelList.get(excelList.size() - 1).getDataDate().split("-");

					String beg = beginDate[0] + "-" + beginDate[1] + "-01";
					String end = endDate[0] + "-" + endDate[1] + "-31";

						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
						importExcelDao.updateStocksByShop(excelList);

				}
			}

			if (displayListDelete.size() > 0) {
				importExcelDao.deleteStockCountByRE(displayListDelete);
				if (displayListRe.size() > 0) {
					DateUtil.remove(displayListRe);

					importExcelDao.saveDisPlaysByRe(displayListRe);
				}
			}

			if (msg.length() > 0) {
				return msg.toString();
			} else {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	}

	@Override
	public XSSFWorkbook exporExcelByCustomer(String spec, String conditions, String[] excelHeader, String title)
			throws Exception {
		String key = null;
		List<HashMap<String, Object>> list = importExcelDao.selectCustomer(conditions, null);

		List<HashMap<String, Object>> modelList = importExcelDao.selectModelByCustomer(null, conditions, spec);
		// 设置 表头宽度
		int[] excelWidth = { 120, 180, 200, 120, 100, 100, 100, 100 };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		sheet.createFreezePane(4, 3, 4, 3);
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));

		// 导出字体样式
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12); // 字体大小
		font.setFontName("Times New Roman");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		XSSFFont fontNo = workbook.createFont();
		fontNo.setFontHeightInPoints((short) 12); // 字体大小
		fontNo.setFontName("Times New Roman");

		XSSFCellStyle styleNo = workbook.createCellStyle();
		styleNo.setFont(fontNo);

		// 导出样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(font);

		// 导出样式
		XSSFCellStyle styleBlue = workbook.createCellStyle();
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleBlue.setBottomBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleBlue.setLeftBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleBlue.setRightBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleBlue.setTopBorderColor(HSSFColor.BLACK.index);
		styleBlue.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBlue.setFont(font);

		XSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
		styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleYellow.setFont(font);

		XSSFCellStyle styleGreen = workbook.createCellStyle();
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
		styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleGreen.setFont(font);

		// 导出字体样式
		XSSFFont fontTwo = workbook.createFont();
		fontTwo.setFontHeightInPoints((short) 12); // 字体大小
		fontTwo.setFontName("Times New Roman");

		// 导出样式
		XSSFCellStyle styleTwo = workbook.createCellStyle();
		styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		styleTwo.setFont(fontTwo);

		XSSFRow rowNotice = sheetNotice.createRow(1);
		XSSFCell cellNotice = rowNotice.createCell(3);
		cellNotice.setCellValue("Notice:");
		cellNotice.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(2);
		XSSFCell cellNoticeTW = rowNotice.createCell(3);
		cellNoticeTW.setCellValue("1. Please fill in the chart with the font Times New Roman in size 12.");
		cellNoticeTW.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(3);
		XSSFCell cellNoticeTh = rowNotice.createCell(3);
		cellNoticeTh.setCellValue("1. 请将字体统一为Times New Roman，字体大小12号。");
		cellNoticeTh.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(4);
		XSSFCell cellNoticeFO = rowNotice.createCell(3);
		cellNoticeFO.setCellValue("2. Store name here must be exactly the same as the ones filled in shopsInfo.");
		cellNoticeFO.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(5);
		XSSFCell cellNoticeFI = rowNotice.createCell(3);
		cellNoticeFI.setCellValue("2. 门店名称必须与门店信息表中所填的名称完全一致。");
		cellNoticeFI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(6);
		XSSFCell cellNoticeSI = rowNotice.createCell(3);
		cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
		cellNoticeSI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(7);
		XSSFCell cellNoticeSE = rowNotice.createCell(3);
		cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
		cellNoticeSE.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(8);
		XSSFCell cellNoticeEI = rowNotice.createCell(3);
		cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
		cellNoticeEI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(9);
		XSSFCell cellNoticeNI = rowNotice.createCell(3);
		cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
		cellNoticeNI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(10);
		XSSFCell cellNoticeTe = rowNotice.createCell(3);
		cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
		cellNoticeTe.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(11);
		XSSFCell cellNoticeEL = rowNotice.createCell(3);
		cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
		cellNoticeEL.setCellStyle(styleNo);
		// --------------------------------------------------------------
		for (int i = 0; i < excelWidth.length; i++) {
			sheet.setColumnWidth(i, 32 * excelWidth[i]);
		}

		XSSFRow row = sheet.createRow(0);
		// 表头数据
		for (int i = 0; i < excelHeader.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);

		}
		XSSFRow rowTwo = sheet.createRow(1);
		XSSFRow rowThree = sheet.createRow(2);

		for (int i = 0; i < modelList.size(); i++) {

			if (modelList.get(i).get("channel_model") != null && !modelList.get(i).get("channel_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i);
				cell0.setCellValue(modelList.get(i).get("channel_model").toString());
				cell0.setCellStyle(styleBlue);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleBlue);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i);
			cellOne.setCellValue("Sold");
			cellOne.setCellStyle(styleBlue);

		}
		int size;
		if (modelList.size() <= 2) {
			size = modelList.size();
		} else {
			size = 2;
		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("channel_model") != null && !modelList.get(i).get("channel_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i + modelList.size());
				cell0.setCellValue(modelList.get(i).get("channel_model").toString());
				cell0.setCellStyle(styleGreen);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i + modelList.size());
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleGreen);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i + modelList.size());
			cellOne.setCellValue("Display");
			cellOne.setCellStyle(styleGreen);

		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("channel_model") != null && !modelList.get(i).get("channel_model").equals("")) {
				XSSFCell cell0 = row.createCell(4 + i + modelList.size() + size);
				cell0.setCellValue(modelList.get(i).get("channel_model").toString());
				cell0.setCellStyle(styleYellow);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(4 + i + modelList.size() + size);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleYellow);
			}

			XSSFCell cellOne = rowThree.createCell(4 + i + modelList.size() + size);
			cellOne.setCellValue("Stocks");
			cellOne.setCellStyle(styleYellow);

		}

		// 表体数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 3);
			HashMap<String, Object> customer = list.get(i);

			// -------------单元格-------------------

			/**
			 * 国家
			 */
			if (i == 0) {
				XSSFCell cell = row.createCell(3);
				cell.setCellValue("dd/MM/yyyy");
				cell.setCellStyle(styleTwo);
			}

			if (customer.get("COUNTRY_NAME") != null && !customer.get("COUNTRY_NAME").equals("")) {
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(customer.get("COUNTRY_NAME").toString());
				cell0.setCellStyle(styleTwo);
			}

			if (customer.get("customer_NAME") != null && !customer.get("customer_NAME").equals("")) {
				XSSFCell cell0 = row.createCell(2);
				cell0.setCellValue(customer.get("customer_NAME").toString());
				cell0.setCellStyle(styleTwo);
			}

			if (customer.get("party_name") != null && !customer.get("party_name").equals("")) {
				XSSFCell cell0 = row.createCell(1);
				cell0.setCellValue(customer.get("party_name").toString());
				cell0.setCellStyle(styleTwo);
			}

		}

		return workbook;
	}

	@Override
	public List<HashMap<String, Object>> selectAllCore() throws Exception {
		return importExcelDao.selectAllCore();
	}

	@Override
	public String read2007ExcelByCountry(File file, String what)
			throws IOException {
		String countrySale="";
		long start=System.currentTimeMillis();
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(0);
			XSSFRow rowPrice = sheet.getRow(1);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS= new HashSet<String>();
			Set<String> setModelSTO= new HashSet<String>();
			for (int j = 2; j < ro.getLastCellNum(); j++) {
				
				HashMap<String, Object> hashMap = new HashMap<>();
				XSSFRow row = null;
				XSSFCell cell = null;
				if(ro.getLastCellNum()>=3){
					row = sheet.getRow(3);

				}
				cells = ro.getCell(j);
				XSSFRow whatRow = sheet.getRow(2);
				if (cells!=null &&
						cells.getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
						
						 !getCellValueByCell(cells).equals("")
						&& row.getCell(0)!=null &&
						!getCellValueByCell(row.getCell(0)).equals("")
						&& row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						) {
					
					String countryName = getCellValueByCell(row.getCell(0));
					String countryId = importExcelDao.selectCountry(countryName);
					String li = "";
					String cond = " AND t.`party_id`='"+countryId+"'";

					int model = modelMapDao.getModelIdByParty(cond,
							getCellValueByCell(cells), li);
					if (model >= 1) {
						hashMap.put("model", getCellValueByCell(cells));
						//判断为so or stock or  sample
						if(whatRow.getCell(j)!=null  &&
								whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
								!whatRow.getCell(j).equals("")
								){
							if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
								hashMap.put("what", "Sold");
								if(rowPrice!=null && rowPrice.getCell(j)!=null
										 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
												
									 ){
									
									 switch (rowPrice.getCell(j).getCellType()) {
									 
								      case HSSFCell.CELL_TYPE_STRING:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)
											+" "	+ getText("excel.error.num") + "<br/>");
								 
								       break;
								 
								      case HSSFCell.CELL_TYPE_FORMULA:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)
											+" "	+ getText("excel.error.num") + "<br/>");
								  
								       break;
								 
								      case HSSFCell.CELL_TYPE_NUMERIC:
								 
								    	  if(rowPrice.getCell(j)
													.getNumericCellValue()!=0){
								    		  hashMap.put("price", rowPrice.getCell(j)
														.getNumericCellValue());
												
											}
								        break;
								 
								      case HSSFCell.CELL_TYPE_ERROR:
								 
								       break;
								 
								      }
							  
									
								}else  {
									if(			countryId!=null
											&& !countryId.equals("")){
										
										Float	price =importExcelDao.selectPrice(countryId,getCellValueByCell(cells));
										if(price!=null && !price.equals("")  && price!=0 &&  price!=0.0){
											
											hashMap.put("price", price);
											
										}else{
											hashMap.put("price", 0);
										
											
										}
										
									}
							
								}
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
								hashMap.put("what", "Stocks");
								boolean one=setModelSTO.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
											+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
								}
								
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
								hashMap.put("what", "DisPlay");
								boolean one=setModelDIS.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") +DateUtil.getExcelColumnLabel (j + 1)+" "
											+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
								}
								
							}else{
								msg.append(getText("excel.error.row")+" "+3+" "+
							getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+ getCellValueByCell(cells)+")"
										+ getText("excel.error.what") + "<br/>");
							}
							
							
						}
					
					}else{
						msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "
						+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
					}
			
					modelList.add(hashMap);
					
				}
				
				
				

			}

			System.out.println(modelList);

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				String countryName="";
				String countryId="";
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					countryName = getCellValueByCell(row.getCell(0));
					countryId= importExcelDao.selectCountry(countryName);
					if (countryId == null  ) {
						msg.append(getText("excel.error.row") + (i + 1)
								+ getText("excel.error.country") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
					}
					
				}else{
					if(
							row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (1)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
				
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate="";
				Float rowExchange=(float) 0;
				
				if(  	row.getCell(0)!=null &&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					
					if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(1)));//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
											+ getText("excel.error.time") + "<br/>");
								}else{
									rowDate=newD;
								
									
								}
						}catch(Exception e){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
									+ getText("excel.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
								+ getText("excel.error.dateNo") + "<br/>");
					}
						
					
				}
			
				
				for (int m = 0; m < modelList.size(); m++) {
					
						
					if( modelList.get(m).get("what")!=null &&  !modelList.get(m).get("what").toString().equals("")){
						
						if(row.getCell(m+ 2)!=null  
								 && row.getCell(m+ 2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(m+ 2).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+3)+" "
											+ getText("excel.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+3)+" "
											+ getText("excel.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
						    	  if(row.getCell(m+2)
											.getNumericCellValue()!=0){
						    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
						    		  modelMap.put("Model", modelList.get(m).get("model"));
						    		  if (countryId != null) {
						    			   countrySale=countryId;
											modelMap.put("Country", countryId == null ? null
													: countryId);
											modelMap.put("CountryName", countryName);
										}
									
						    		
								
							
									if(rowDate!=null && !rowDate.equals("")){
										
										modelMap.put("DataDate", rowDate);
										if ( countryId!=null && !countryId.equals("")	
												&& modelList.get(m).get("price")!=null 
												&&  !modelList.get(m).get("price").toString().equals("")
												&& Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
														String []days=rowDate.split("-");
														String begin=days[0]+"-"+days[1]+"-01";
														String end=days[0]+"-"+days[1]+"-31";
														Float exchange=importExcelDao.selectExchange(countryId, begin, end,rowDate);
														if(exchange==null){
															msg.append(countryName+" " 
																	+ getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "<br/>");
														}else{
															rowExchange=exchange;
														}
														
												}
								
									}
									
									if(rowExchange!=null && rowExchange!=0  && rowExchange!=0.0){
										modelMap.put("exchange", rowExchange);
									}
									if( modelList.get(m).get("price")!=null &&  !modelList.get(m).get("price").toString().equals("")){
										modelMap.put("Price", modelList.get(m).get("price"));
										
									}
						    		  if( modelList.get(m).get("what").toString().equals("Sold")){
						    			  modelMap.put("sold", (int) row.getCell(m+ 2)
													.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("DisPlay")){
											
											  modelMap.put("display", (int) row.getCell(m+ 2)
														.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("Stocks")){
											
											  modelMap.put("stocks", (int) row.getCell(m+ 2)
														.getNumericCellValue());
										}
										
						    		  allModelList.add(modelMap);
										
									}
						       
						    	  
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
							
							
						
						}
						
						
						
						
						
					}
					
				
					
				
					
						
							
					
				}
				
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

					if(j>=0){
						cell = row.getCell(j);
					}
					
					if (cell == null || cell.getCellType()!=HSSFCell.CELL_TYPE_BLANK) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell
									.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}
			String userId="";
			if(WebPageUtil.getLoginedUserId()!=null){
				userId=(WebPageUtil.getLoginedUserId());
			}
			System.out.println("==========size==========="+allModelList);
			List<ImportExcel> excelList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockListByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayList=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListReByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListByupdate=new ArrayList<ImportExcel>();
			//System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
				int row = 0;
				
					if (
							
							
							
							 allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							
							
							&& allModelList.get(i).get("Country")!=null
							&& allModelList.get(i).get("Country")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
					
					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate")
								.toString());
						
						
						excel.setCountryId(allModelList.get(i).get("Country")
								.toString());
						
				if(
						allModelList.get(i).get("sold") != null
						&& Integer.parseInt(allModelList.get(i).get("sold")
								.toString()) != 0
						&& allModelList.get(i)
						.get("Price")!=null ){
					
					BigDecimal bd = new BigDecimal(allModelList.get(i)
							.get("sold").toString()); 
				
					int qty=bd.intValue();
					excel.setSaleQty(bd.intValue());
					excel.setSource("PC");
					
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
					nf.setGroupingUsed(false);  
					
					
					excel.setSalePrice(Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString()))));
					
					double price=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString())));
					
					
					bd = new BigDecimal(qty*price); 
					
					excel.setAmt(String.valueOf(bd.longValue()));
					excel.setH_qty(qty);
					if(allModelList.get(i).get("exchange")!=null 
							&& 
							Float.parseFloat(allModelList.get(i).get("exchange").toString())!=0
							){
						
						Float exchange=Float.parseFloat(allModelList.get(i).get("exchange").toString());
					
						if(exchange!=null){
							bd = new BigDecimal(price*exchange); 
							double Hprice=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.setH_price(Hprice);
							bd = new BigDecimal(qty*Hprice);
							BigDecimal HAmt=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
							excel.setH_amt(String.valueOf(HAmt));
						}
						
					}else {
						excel.setH_price(0);
						excel.setH_amt(String.valueOf('0'));
					}
					
				excelList.add(excel);
						/*row = importExcelDao.saveSales(excel);*/
						/*if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.saleInsert") + "<br/>");
=======
						
					}
					
				excelList.add(excel);
				System.out.println("======listSize============"+excelList.size());
						/*row = importExcelDao.saveSales(excel);*/
						/*if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.saleInsert") + "<br/>");
>>>>>>> .r14220

						}*/
					
				}else if(allModelList.get(i).get("stocks") != null
						&& Integer.parseInt(allModelList.get(i).get("stocks")
								.toString()) != 0){
					excel.setStockQty(Integer.parseInt(allModelList.get(i)
							.get("stocks").toString()));
				
					
					
					importExcelDao.deleteStockCountByRECountry(allModelList
							.get(i).get("Country").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						stockList.add(excel);
					/*}else{
						excel.setId(rowsRE);
						stockListByupdate.add(excel);
					}*/
					
					
				}else if(allModelList.get(i).get("display") != null
						&& Integer.parseInt(allModelList.get(i).get("display")
								.toString()) != 0){
					excel.setDisPlayQty(Integer.parseInt(allModelList.get(i)
							.get("display").toString()));
				
				 importExcelDao.deleteDisplayCountByRECountry(allModelList
							.get(i).get("Country").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						displayListRe.add(excel);
					/*}else{
						excel.setId(rowsRE);
						displayListReByupdate.add(excel);
					}*/
					
					
				}
				
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			if(displayList.size()>0){
				importExcelDao.saveDisPlays(displayList);
			}
			if(stockList.size()>0){
				DateUtil.remove(stockList); 
				importExcelDao.saveStocks(stockList);
			}
			if(displayListByupdate.size()>0){
				importExcelDao.updateDisPlay(displayListByupdate);
			}
			if(stockListByupdate.size()>0){
				importExcelDao.updateStockRE(stockListByupdate);
			}
			
			if(excelList.size()>0){
				int row = importExcelDao.saveSales(excelList);
				if(row>0){
					DateUtil.ListSort(excelList);
					String [] beginDate=excelList.get(0).getDataDate().split("-");
					String [] endDate=excelList.get(excelList.size()-1).getDataDate().split("-");
					
					String beg=beginDate[0]+"-"+beginDate[1]+"-01";
					String end=endDate[0]+"-"+endDate[1]+"-31";
					
					if(what.equals("shop")){
						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
					}
					
				}
			}
			if(displayListRe.size()>0){
				DateUtil.remove(displayListRe); 
				importExcelDao.saveDisPlaysByRe(displayListRe);
			}
			if(displayListReByupdate.size()>0){
				importExcelDao.updateDisPlayRE(displayListReByupdate);
			}
			System.out.println("=========================Time"+(System.currentTimeMillis()-start));

			if (msg.length() > 0) {
				return msg.toString();
			} else {
				
				return "";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
		
	}
	@Override
	public XSSFWorkbook exporExcelByCountry(String spec, String conditions, String[] excelHeader, String title)
			throws Exception {
		String key = null;
		List<HashMap<String, Object>> list = importExcelDao.selectCountryList(conditions, null);

		List<HashMap<String, Object>> modelList = importExcelDao.selectModel(null, conditions, spec);
		// 设置 表头宽度
		int[] excelWidth = { 120, 180, 120, 120, 100, 100, 100, 100 };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		sheet.createFreezePane(4, 3, 4, 3);
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));

		// sheet.addMergedRegion(new CellRangeAddress(0, 2,2, 2));
		// sheet.addMergedRegion(new CellRangeAddress(0, 2,3, 3));

		// 导出字体样式
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12); // 字体大小
		font.setFontName("Times New Roman");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		XSSFFont fontNo = workbook.createFont();
		fontNo.setFontHeightInPoints((short) 12); // 字体大小
		fontNo.setFontName("Times New Roman");

		XSSFCellStyle styleNo = workbook.createCellStyle();
		styleNo.setFont(fontNo);

		// 导出样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(font);

		// 导出样式
		XSSFCellStyle styleBlue = workbook.createCellStyle();
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleBlue.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleBlue.setBottomBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleBlue.setLeftBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleBlue.setRightBorderColor(HSSFColor.BLACK.index);
		styleBlue.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleBlue.setTopBorderColor(HSSFColor.BLACK.index);
		styleBlue.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBlue.setFont(font);

		XSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
		styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
		styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleYellow.setFont(font);

		XSSFCellStyle styleGreen = workbook.createCellStyle();
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
		styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
		styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
		styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleGreen.setFont(font);

		// 导出字体样式
		XSSFFont fontTwo = workbook.createFont();
		fontTwo.setFontHeightInPoints((short) 12); // 字体大小
		fontTwo.setFontName("Times New Roman");

		// 导出样式
		XSSFCellStyle styleTwo = workbook.createCellStyle();
		styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		styleTwo.setFont(fontTwo);

		XSSFRow rowNotice = sheetNotice.createRow(1);
		XSSFCell cellNotice = rowNotice.createCell(3);
		cellNotice.setCellValue("Notice:");
		cellNotice.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(2);
		XSSFCell cellNoticeTW = rowNotice.createCell(3);
		cellNoticeTW.setCellValue("1. Please fill in the chart with the font Times New Roman in size 12.");
		cellNoticeTW.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(3);
		XSSFCell cellNoticeTh = rowNotice.createCell(3);
		cellNoticeTh.setCellValue("1. 请将字体统一为Times New Roman，字体大小12号。");
		cellNoticeTh.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(6);
		XSSFCell cellNoticeSI = rowNotice.createCell(3);
		cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
		cellNoticeSI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(7);
		XSSFCell cellNoticeSE = rowNotice.createCell(3);
		cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
		cellNoticeSE.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(8);
		XSSFCell cellNoticeEI = rowNotice.createCell(3);
		cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
		cellNoticeEI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(9);
		XSSFCell cellNoticeNI = rowNotice.createCell(3);
		cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
		cellNoticeNI.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(10);
		XSSFCell cellNoticeTe = rowNotice.createCell(3);
		cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
		cellNoticeTe.setCellStyle(styleNo);

		rowNotice = sheetNotice.createRow(11);
		XSSFCell cellNoticeEL = rowNotice.createCell(3);
		cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
		cellNoticeEL.setCellStyle(styleNo);
		// --------------------------------------------------------------
		for (int i = 0; i < excelWidth.length; i++) {
			sheet.setColumnWidth(i, 32 * excelWidth[i]);
		}

		XSSFRow row = sheet.createRow(0);
		// 表头数据
		for (int i = 0; i < excelHeader.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);

		}
		XSSFRow rowTwo = sheet.createRow(1);
		XSSFRow rowThree = sheet.createRow(2);

		for (int i = 0; i < modelList.size(); i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(2 + i);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleBlue);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(2 + i);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleBlue);
			}

			XSSFCell cellOne = rowThree.createCell(2 + i);
			cellOne.setCellValue("Sold");
			cellOne.setCellStyle(styleBlue);

		}
		int size;
		if (modelList.size() <= 2) {
			size = modelList.size();
		} else {
			size = 2;
		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(2 + i + modelList.size());
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleGreen);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(2 + i + modelList.size());
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleGreen);
			}

			XSSFCell cellOne = rowThree.createCell(2 + i + modelList.size());
			cellOne.setCellValue("Display");
			cellOne.setCellStyle(styleGreen);

		}

		for (int i = 0; i < size; i++) {

			if (modelList.get(i).get("branch_model") != null && !modelList.get(i).get("branch_model").equals("")) {
				XSSFCell cell0 = row.createCell(2 + i + modelList.size() + size);
				cell0.setCellValue(modelList.get(i).get("branch_model").toString());
				cell0.setCellStyle(styleYellow);

			}

			if (modelList.get(i).get("price") != null
					&& Double.parseDouble(modelList.get(i).get("price").toString()) != 0.0) {
				XSSFCell cell = rowTwo.createCell(2 + i + modelList.size() + size);
				cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));
				cell.setCellStyle(styleYellow);
			}

			XSSFCell cellOne = rowThree.createCell(2 + i + modelList.size() + size);
			cellOne.setCellValue("Stocks");
			cellOne.setCellStyle(styleYellow);

		}

		// 表体数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 3);
			HashMap<String, Object> customer = list.get(i);

			// -------------单元格-------------------

			/**
			 * 国家
			 */
			if (i == 0) {
				XSSFCell cell = row.createCell(1);
				cell.setCellValue("dd/MM/yyyy");
				cell.setCellStyle(styleTwo);
			}

			if (customer.get("COUNTRY_NAME") != null && !customer.get("COUNTRY_NAME").equals("")) {
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(customer.get("COUNTRY_NAME").toString());
				cell0.setCellStyle(styleTwo);
			}

		}

		return workbook;
	}

	@Override
	public String selectSoType(String country) throws Exception {

		String result = "";

		result = importExcelDao.selectSoType(country);

		if (result == null || result.equals("")) {
			result = "admin";
		}

		return result;

	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	private boolean isMergedRegion(XSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public String getMergedRegionValue(XSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					XSSFRow fRow = sheet.getRow(firstRow);
					XSSFCell fCell = fRow.getCell(firstColumn);
					return getCellValueByCell(fCell);
				}
			}
		}

		return null;
	}

	@Override
	public String read2007ExcelByVn(File file, String what) throws IOException {
		String countrySale = "";
		long start = System.currentTimeMillis();
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(2);
			XSSFRow rowPrice = sheet.getRow(4);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS = new HashSet<String>();
			Set<String> setModelSTO = new HashSet<String>();
			String countryId = WebPageUtil.getLoginedUser().getPartyId();
			ArrayList arrayList = new ArrayList<>();
			for (int j = 27; j < ro.getLastCellNum(); j++) {
				cells = ro.getCell(j);

				HashMap<String, Object> hashMap = new HashMap<>();

				String values = "";
				boolean isMerge = isMergedRegion(sheet, ro.getRowNum(), j);
				XSSFRow whatRow = sheet.getRow(3);
				if (cells != null && cells.getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& getCellValueByCell(cells)!= null &&
						!getCellValueByCell(cells).equals("")

				) {

					// 判断是否具有合并单元格

					String li = "";
					String cond   = " AND t.`party_id`='"+countryId+"'";
					if (values.contains("SUB-TOTAL") || values.contains("TTL")) {
						arrayList.add(j);
						hashMap.put("model", j);
						hashMap.put("what", "sum");
					} else {

						int model = modelMapDao.getModelIdByParty(cond, getCellValueByCell(cells), li);
						if (model >= 1) {
							hashMap.put("model", getCellValueByCell(cells));
							// 判断为so or stock or sample
							if (whatRow.getCell(j) != null
									&& whatRow.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
									&& !getCellValueByCell(whatRow.getCell(j)).equals("")) {
								if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")) {
									hashMap.put("what", "Sold");
									if (rowPrice != null && rowPrice.getCell(j) != null
											&& rowPrice.getCell(j).getCellType() != HSSFCell.CELL_TYPE_BLANK
											&& !rowPrice.getCell(j).equals("")) {

										switch (rowPrice.getCell(j).getCellType()) {

										case HSSFCell.CELL_TYPE_STRING:

											msg.append(getText("excel.error.row") + (5)+" " + getText("excel.error.cell")
													+ DateUtil.getExcelColumnLabel(j + 1)+ " " + getText("excel.error.num")
													+ "<br/>");

											break;

										case HSSFCell.CELL_TYPE_FORMULA:

											msg.append(getText("excel.error.row") + (5)+" " + getText("excel.error.cell")
													+ DateUtil.getExcelColumnLabel(j + 1) + " "+ getText("excel.error.num")
													+ "<br/>");

											break;

										case HSSFCell.CELL_TYPE_NUMERIC:

											if (rowPrice.getCell(j).getNumericCellValue() != 0) {
												hashMap.put("price", rowPrice.getCell(j).getNumericCellValue());

											}
											break;

										case HSSFCell.CELL_TYPE_ERROR:

											break;

										}

									} else {
										if (countryId != null && !countryId.equals("")) {

											Float price = importExcelDao.selectPrice(countryId,
													getCellValueByCell(cells));
											if (price != null && !price.equals("") && price != 0 && price != 0.0) {

												hashMap.put("price", price);

											} else {
												hashMap.put("price", 0);
												// msg.append(getText("excel.error.line") +
												// DateUtil.getExcelColumnLabel(j +1)+" "+cells.getStringCellValue()+" "
												// + getText("excel.error.price") + "<br/>");

											}

										}

									}
								} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase()
										.equals("stocks")) {
									hashMap.put("what", "Stocks");
									boolean one = setModelSTO.add(getCellValueByCell(cells));
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+ " "
												+ " " + getText("excel.error.stoRE") + "  ("
												+ getCellValueByCell(cells) + ") " + "<br/>");
									}

								} else if (getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase()
										.equals("display")) {
									hashMap.put("what", "DisPlay");
									boolean one = setModelDIS.add(getCellValueByCell(cells));
									if (!one) {
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+ " "
												+ " " + getText("excel.error.disRE") + "  ("
												+getCellValueByCell(cells) + ") " + "<br/>");
									}

								} else {
									msg.append(getText("excel.error.row") + " " + 6 + " " + getText("excel.error.line")
											+ DateUtil.getExcelColumnLabel(j + 1) + " " + "("
											+ getCellValueByCell(cells)+ ")" + getText("excel.error.what") + "<br/>");
								}

							}

						} else {
							msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+ " "
									+ getText("excel.error.model") + "(" + getCellValueByCell(cells) + ")" + "<br/>");

						}

					}

				} else {
					arrayList.add(j);
					hashMap.put("model", j);
					hashMap.put("what", "sum");
				}
				modelList.add(hashMap);
			}
			System.out.println("==求和列=" + arrayList);
			System.out.println(modelList);
			System.out.println(modelList.size());

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 8; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();

				String shopName = "";
				Excel shop = null;
				if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& !getCellValueByCell(row.getCell(5)).equals("")

				) {
					shopName = getCellValueByCell(row.getCell(5));
					shop = importExcelDao.getShopByNames(shopName);
					if (shop == null) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.shop") + "("
								+ getCellValueByCell(row.getCell(5)) + ")" + "<br/>");
					}

				} else {
					if (row.getCell(6) != null && row.getCell(6).getCellType() != HSSFCell.CELL_TYPE_BLANK
							&& !getCellValueByCell(row.getCell(6)).equals("")) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(6) + " " + getText("excel.error.null") + "<br/>");
					}

				}

				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& !getCellValueByCell(row.getCell(5)).equals("")) {

					if (row.getCell(6) != null && row.getCell(6).getCellType() != HSSFCell.CELL_TYPE_BLANK
							&& !getCellValueByCell(row.getCell(6)).equals("")) {
						try {
						
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(6)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							if (dt1.getTime() < dt2.getTime()) {
								msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(6 + 1) + " "+ getText("excel.error.time") + "<br/>");
							} else {
								rowDate = newD;
							

							}
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(6 + 1) + " "+ getText("excel.error.date") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(6 + 1) + " "+ getText("excel.error.dateNo") + "<br/>");
					}

				}

				for (int m = 0; m < modelList.size(); m++) {

					int qtyCell = 27 + m;
					if (modelList.get(m).get("what") != null && !modelList.get(m).get("what").toString().equals("")
							&& (!modelList.get(m).get("what").toString().equals("sum")
									&& !modelList.get(m).get("model").toString().equals(qtyCell + ""))

					) {

						if (row.getCell(qtyCell) != null
								&& row.getCell(qtyCell).getCellType() != HSSFCell.CELL_TYPE_BLANK

						) {
							switch (row.getCell(qtyCell).getCellType()) {

							case HSSFCell.CELL_TYPE_STRING:
								msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(qtyCell + 1)+ " " + getText("excel.error.num")
										+ "<br/>");
								break;

							case HSSFCell.CELL_TYPE_FORMULA:
								try {

									FormulaEvaluator evaluator = row.getCell(qtyCell).getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
						        	evaluator.evaluateFormulaCell(row.getCell(qtyCell));
						        	CellValue cellValues = evaluator.evaluate(row.getCell(qtyCell));
						        	double celldata = cellValues.getNumberValue();
						        	
									/*msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
											+ DateUtil.getExcelColumnLabel(qtyCell+1)+ " " + getText("excel.error.num") + "<br/>");*/
						        	if (celldata != 0 && celldata!=0.0) {
										HashMap<String, Object> modelMap = new HashMap<String, Object>();
										modelMap.put("Model", modelList.get(m).get("model"));
										if (countryId != null) {
											countrySale = countryId;
											modelMap.put("Country", countryId == null ? null : countryId);
										}

										if (shop != null) {
											modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
											modelMap.put("StoreName", shopName);
										}

										

										if (rowDate != null && !rowDate.equals("")) {

											modelMap.put("DataDate", rowDate);
											if (countryId != null && !countryId.equals("")
													&&
													modelList.get(m).get("price") != null
													&& !modelList.get(m).get("price").toString().equals("")
													&& Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
													) {
														String[] days = rowDate.split("-");
														String begin = days[0] + "-" + days[1] + "-01";
														String end = days[0] + "-" + days[1] + "-31";
														Float exchange = importExcelDao.selectExchange(countryId, begin, end, rowDate);
														if (exchange == null) {
															msg.append(getText("excel.error.exchange") + " " + days[1] + "/" + days[0]
																	+ "<br/>");
														} else {
															modelMap.put("exchange", exchange);
														}

													}
										}

										/*if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
											modelMap.put("exchange", rowExchange);
										}*/
										if (modelList.get(m).get("price") != null
												&& !modelList.get(m).get("price").toString().equals("")) {
											modelMap.put("Price", modelList.get(m).get("price"));

										}
										if (modelList.get(m).get("what").toString().equals("Sold")) {
											modelMap.put("sold", (int)celldata);
										} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

											modelMap.put("display", (int)celldata);
										} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

											modelMap.put("stocks", (int)celldata);
										}

										allModelList.add(modelMap);

									}
						        	break;
								
								}catch (Exception e) {
									msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(qtyCell + 1)+ " " + getText("excel.error.formulaError") + "<br/>");
									break;
								}

							case HSSFCell.CELL_TYPE_NUMERIC:

								if (row.getCell(qtyCell).getNumericCellValue() != 0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelList.get(m).get("model"));
									if (countryId != null) {
										countrySale = countryId;
										modelMap.put("Country", countryId == null ? null : countryId);
									}

									if (shop != null) {
										modelMap.put("Store", shop.getShopId() == null ? null : shop.getShopId());
										modelMap.put("StoreName", shopName);
									}

									if (rowDate != null && !rowDate.equals("")) {

										modelMap.put("DataDate", rowDate);

									}

									if (rowExchange != null && rowExchange != 0 && rowExchange != 0.0) {
										modelMap.put("exchange", rowExchange);
									}
									if (modelList.get(m).get("price") != null
											&& !modelList.get(m).get("price").toString().equals("")) {
										modelMap.put("Price", modelList.get(m).get("price"));

									}
									if (modelList.get(m).get("what").toString().equals("Sold")) {
										modelMap.put("sold", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("DisPlay")) {

										modelMap.put("display", (int) row.getCell(qtyCell).getNumericCellValue());
									} else if (modelList.get(m).get("what").toString().equals("Stocks")) {

										modelMap.put("stocks", (int) row.getCell(qtyCell).getNumericCellValue());
									}

									allModelList.add(modelMap);

								}

								break;

							case HSSFCell.CELL_TYPE_ERROR:

								break;

							}

						}

					}

				}

				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

					if (j >= 0) {
						cell = row.getCell(j);
					}

					if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}
			String userId = "";
			if (WebPageUtil.getLoginedUserId() != null) {
				userId = (WebPageUtil.getLoginedUserId());
			}
			System.out.println("==========size===========" + allModelList);
			List<ImportExcel> excelList = new ArrayList<ImportExcel>();
			List<ImportExcel> stockList = new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe = new ArrayList<ImportExcel>();

			List<ImportExcel> displayListDelete = new ArrayList<ImportExcel>();
			List<ImportExcel> stockListDelete = new ArrayList<ImportExcel>();
			System.out.println("=error=="+msg);
			if (msg.length() <= 0) {
				for (int i = 0; i < allModelList.size(); i++) {

					if (

					allModelList.get(i).get("Store") != null && allModelList.get(i).get("Store") != ""

							&& allModelList.get(i).get("Model") != null && allModelList.get(i).get("Model") != ""

							&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""

							&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
							&& userId != null && userId != ""

					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate").toString());
						excel.setShopId(Integer.parseInt(allModelList.get(i).get("Store").toString()));

						excel.setCountryId(allModelList.get(i).get("Country").toString());

						if (allModelList.get(i).get("sold") != null
								&& Integer.parseInt(allModelList.get(i).get("sold").toString()) != 0
								&& allModelList.get(i).get("Price") != null) {

							BigDecimal bd = new BigDecimal(allModelList.get(i).get("sold").toString());

							int qty = bd.intValue();
							excel.setSaleQty(bd.intValue());
							excel.setSource("PC");

							java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
							nf.setGroupingUsed(false);

							excel.setSalePrice(Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString()))));

							double price = Double.parseDouble(
									nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString())));

							bd = new BigDecimal(qty * price);

							excel.setAmt(String.valueOf(bd.longValue()));
							excel.setH_qty(qty);
							if (allModelList.get(i).get("exchange") != null
									&& Float.parseFloat(allModelList.get(i).get("exchange").toString()) != 0) {

								Float exchange = Float.parseFloat(allModelList.get(i).get("exchange").toString());
								
								if (exchange != null) {
									bd = new BigDecimal(price * exchange);
									double Hprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									excel.setH_price(Hprice);
									bd = new BigDecimal(qty * Hprice);
									BigDecimal HAmt = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
									excel.setH_amt(String.valueOf(HAmt));
								}

							}else {
								excel.setH_price(0);
								excel.setH_amt(String.valueOf('0'));
							}

							excelList.add(excel);

						} else if (allModelList.get(i).get("stocks") != null
								&& Integer.parseInt(allModelList.get(i).get("stocks").toString()) != 0) {
							excel.setStockQty(Integer.parseInt(allModelList.get(i).get("stocks").toString()));

							stockListDelete.add(excel);

							excel.setClassId(1);
							stockList.add(excel);

						} else if (allModelList.get(i).get("display") != null
								&& Integer.parseInt(allModelList.get(i).get("display").toString()) != 0) {
							excel.setDisPlayQty(Integer.parseInt(allModelList.get(i).get("display").toString()));

							displayListDelete.add(excel);

							excel.setClassId(1);
							displayListRe.add(excel);

						}

					}
				}

				// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}

			if (stockListDelete.size() > 0) {
				importExcelDao.deleteStockCountByRE(stockListDelete);
				if (stockList.size() > 0) {
					DateUtil.remove(stockList);

					importExcelDao.saveStocks(stockList);
				}
			}

			if (excelList.size() > 0) {
				int row = importExcelDao.saveSales(excelList);
				if (row > 0) {
					DateUtil.ListSort(excelList);
					String[] beginDate = excelList.get(0).getDataDate().split("-");
					String[] endDate = excelList.get(excelList.size() - 1).getDataDate().split("-");

					String beg = beginDate[0] + "-" + beginDate[1] + "-01";
					String end = endDate[0] + "-" + endDate[1] + "-31";
					String countryList = "";

					if (what.equals("shop")) {
						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
						importExcelDao.updateStocksByShop(excelList);
					}

				}
			}
			if (displayListDelete.size() > 0) {
				importExcelDao.deleteDisplayCountByRE(displayListDelete);
				if (displayListRe.size() > 0) {
					DateUtil.remove(displayListRe);

					importExcelDao.saveDisPlaysByRe(displayListRe);
				}
			}

			System.out.println("=========================Time" + (System.currentTimeMillis() - start));

			if (msg.length() > 0) {
				return msg.toString();
			} else {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}

	}

	 private static String getCellValueByCell(XSSFCell xssfCell) {
	        //判断是否为null或空串
	        if (xssfCell==null || xssfCell.toString().trim().equals("")) {
	            return "";
	        }
	        String cellValue = "";
	        int cellType=xssfCell.getCellType();
	        try {
	        if(cellType==HSSFCell.CELL_TYPE_FORMULA){ //表达式类型
	        	FormulaEvaluator evaluator = xssfCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
	        	evaluator.evaluateFormulaCell(xssfCell);
	        	CellValue cellValues = evaluator.evaluate(xssfCell);
	        	Double celldata = cellValues.getNumberValue();
	        	return celldata+"";
	        }
	    	}catch (Exception e) {
				return 0+"";
			}
	        switch (cellType) {
	        case HSSFCell.CELL_TYPE_STRING: //字符串类型
	            cellValue= xssfCell.getStringCellValue().trim();
	            break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:  //布尔类型
	            cellValue = String.valueOf(xssfCell.getBooleanCellValue()); 
	            break; 
	        case HSSFCell.CELL_TYPE_NUMERIC: //数值类型
	             if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {  //判断日期类型
	                 cellValue =    format.format(xssfCell.getDateCellValue());
	             } else {  //否
	                 cellValue = new DecimalFormat("#.######").format(xssfCell.getNumericCellValue()); 
	             } 
	            break;
	        default: //其它类型，取空串吧
	            cellValue = "";
	            break;
	        }
	        return cellValue;
	    }

	 @Override
		public String read2007ExcelByTh(File file, String what) throws IOException {
			long start = System.currentTimeMillis();
			StringBuffer msg = new StringBuffer();
			SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			List<List<Object>> list = new LinkedList<List<Object>>();
			try {
				XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
				XSSFSheet sheet = xwb.getSheetAt(0);
				Object value = null;
				XSSFRow ro = sheet.getRow(0);
				XSSFCell cells = null;
				List<HashMap<String, Object>> dateList = new LinkedList<HashMap<String, Object>>();
				Set<String> setModelDIS = new HashSet<String>();
				Set<String> setModelSTO = new HashSet<String>();
				String date = getCellValueByCell(sheet.getRow(1).getCell(5)).toLowerCase();
				String[] sourceStrArray = date.split("/");
				String year = "";
				String month = "";
				for (int i = 0; i < sourceStrArray.length; i++) {
					year = sourceStrArray[sourceStrArray.length - 1];
					month = sourceStrArray[sourceStrArray.length - 2];
				}

				for (int j = 1; j < 32; j++) {

					HashMap<String, Object> hashMap = new HashMap<>();
					String newD= year + "-" + month + "-" + j;
					Date dates=dfd.parse(newD);
					newD=dfd.format(dates);
					hashMap.put("date",newD);
					
					dateList.add(hashMap);

				}

				String customerName = sheet.getSheetName();
				String customerId="";
				if (customerName != null && !customerName.equals("")) {
					 customerId = importExcelDao.selectCustomerByName(customerName, 4 + "");
					if (customerId == null || customerId.equals("")) {
						msg.append(getText("excel.error.customer") + "(" + customerName + ")" + "<br/>");
					}

				} else {
					msg.append(getText("excel.error.sheetcustomer") + "<br/>");

				}
				String countryId = WebPageUtil.getLoginedUser().getPartyId();
				List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
				for (int i = 4; i <= sheet.getPhysicalNumberOfRows(); i++) {

					XSSFRow row = null;
					XSSFCell cell = null;
					row = sheet.getRow(i);
					if (row == null) {
						continue;
					}
					if (getCellValueByCell(sheet.getRow(i).getCell(0)).toLowerCase().contains("grand total-qty")
							|| getCellValueByCell(sheet.getRow(i).getCell(1)).toLowerCase().contains("grand total-qty")) {
						continue;
					}
					
					if (getCellValueByCell(sheet.getRow(i).getCell(0)).toLowerCase().contains("total discon-qty")
							|| getCellValueByCell(sheet.getRow(i).getCell(1)).toLowerCase().contains("total discon-qty")) {
						break;
					}

					if (getCellValueByCell(sheet.getRow(i).getCell(1)).toLowerCase().contains("ratio")) {
						continue;
					}
					List<Object> linked = new LinkedList<Object>();

					double price = 0.0;
					String modelName = "";
					if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

						String li = "";
						String cond = " AND t.`party_id`='" + countryId + "'";

						int model = modelMapDao.getModelIdByParty(cond, getCellValueByCell(row.getCell(1)), li);
						if (model >= 1) {
							modelName = getCellValueByCell(row.getCell(1));

							if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
								switch (row.getCell(2).getCellType()) {

								case HSSFCell.CELL_TYPE_STRING:
									msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
											+ DateUtil.getExcelColumnLabel(3) +"  "+ getText("excel.error.num") + "<br/>");
									break;

								case HSSFCell.CELL_TYPE_FORMULA:
									try {
										FormulaEvaluator evaluator = row.getCell(2).getSheet().getWorkbook().getCreationHelper()
												.createFormulaEvaluator();
										evaluator.evaluateFormulaCell(row.getCell(2));
										CellValue cellValues = evaluator.evaluate(row.getCell(2));
										double celldata = cellValues.getNumberValue();

										if (celldata != 0 && celldata != 0.0) {
											price = celldata;

										}
									}catch (Exception e) {
										msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")
										+ DateUtil.getExcelColumnLabel(3) +"  "+ getText("excel.error.formulaError") + "<br/>");
									}
									

									break;
									
								case HSSFCell.CELL_TYPE_NUMERIC:
									price = row.getCell(2).getNumericCellValue();

									break;

								case HSSFCell.CELL_TYPE_ERROR:

									break;

								}
							} else {
								if (countryId != null && !countryId.equals("")) {

									Float prices = importExcelDao.selectPrice(countryId,
											getCellValueByCell(row.getCell(1)));
									if (prices != null && !prices.equals("")) {

										price = prices;

									} else {
										price = 0;
									}

								}

							}

						}else {


							Excel channelModel = importExcelDao.selectCustomerModel(getCellValueByCell(row.getCell(1)), countryId,customerId);
							if(channelModel==null){
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "+ DateUtil.getExcelColumnLabel(2) +"  "
										+ getText("excel.error.model") +"("+ getCellValueByCell(row.getCell(1))+")"+ "<br/>");
							}else{
								modelName=channelModel.getModel();

								if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
									switch (row.getCell(2).getCellType()) {

									case HSSFCell.CELL_TYPE_STRING:
										msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "
												+ DateUtil.getExcelColumnLabel(3) +""+ getText("excel.error.num") + "<br/>");
										break;

									case HSSFCell.CELL_TYPE_FORMULA:
										try {
											FormulaEvaluator evaluator = row.getCell(2).getSheet().getWorkbook().getCreationHelper()
													.createFormulaEvaluator();
											evaluator.evaluateFormulaCell(row.getCell(2));
											CellValue cellValues = evaluator.evaluate(row.getCell(2));
											double celldata = cellValues.getNumberValue();

											if (celldata != 0 && celldata != 0.0) {
												price = celldata;

											}

										}catch (Exception e) {
											msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "
													+ DateUtil.getExcelColumnLabel(3) +"  "+ getText("excel.error.formulaError") + "<br/>");
										}
										
										break;

									case HSSFCell.CELL_TYPE_NUMERIC:
										price = row.getCell(2).getNumericCellValue();

										break;

									case HSSFCell.CELL_TYPE_ERROR:

										break;

									}
								} else {
									if (countryId != null && !countryId.equals("")) {

										Float prices = importExcelDao.selectPrice(countryId,
												getCellValueByCell(row.getCell(1)));
										if (prices != null && !prices.equals("")) {

											price = prices;

										} else {
											price = 0;
										}

									}

								}

							}
							
							
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "
								+ DateUtil.getExcelColumnLabel(2) + "  "+getText("excel.error.model") + "("
								+ getCellValueByCell(row.getCell(1)) + ")" + "<br/>");

					}

					for (int m = 0; m < dateList.size(); m++) {
						if (row.getCell(m + 5) != null && row.getCell(m + 5).getCellType() != HSSFCell.CELL_TYPE_BLANK

						) {
							switch (row.getCell(m + 5).getCellType()) {

							case HSSFCell.CELL_TYPE_STRING:
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "
										+ DateUtil.getExcelColumnLabel(m + 5) + getText("excel.error.num") + "<br/>");
								break;

							case HSSFCell.CELL_TYPE_FORMULA:

								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.cell")+" "
										+ DateUtil.getExcelColumnLabel(m + 5) + getText("excel.error.num") + "<br/>");

								break;

							case HSSFCell.CELL_TYPE_NUMERIC:

								if (row.getCell(m + 5).getNumericCellValue() != 0) {
									HashMap<String, Object> modelMap = new HashMap<String, Object>();
									modelMap.put("Model", modelName);
									String newD = dateList.get(m).get("date").toString();
									
									/*if (dateList.get(m).get("exchange") != null) {
										modelMap.put("exchange", dateList.get(m).get("exchange"));
									}
									*/
									if (countryId != null) {
										modelMap.put("Country", countryId == null ? null : countryId);

									}
									modelMap.put("Customer", customerId);
									if(price!=0 && price!=0.0) {
										String[] days = newD.split("-");
										String begin = days[0] + "-" + days[1] + "-01";
										String end = days[0] + "-" + days[1] + "-31";
										Float exchange = importExcelDao.selectExchange(4+"", begin, end, newD);
										if (exchange == null) {
											msg.append(
													getText("excel.error.exchange") + " " + days[1] + "/" + days[0] + "<br/>");
										} else {
											modelMap.put("exchange", exchange);
										}
									}
									
									
									modelMap.put("DataDate", newD);
									modelMap.put("Price", price);
									modelMap.put("sold", (int) row.getCell(m + 5).getNumericCellValue());

									allModelList.add(modelMap);

								}

								break;

							case HSSFCell.CELL_TYPE_ERROR:

								break;

							}

						}

					}

					for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

						if (j >= 0) {
							cell = row.getCell(j);
						}

						if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
							continue;
						}
						DecimalFormat df = new DecimalFormat("0");// 鏍煎紡鍖� number
																	// String 瀛楃
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 鏍煎紡鍖栨棩鏈熷瓧绗︿覆
						DecimalFormat nf = new DecimalFormat("0");// 鏍煎紡鍖栨暟瀛�
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:
							if ("@".equals(cell.getCellStyle().getDataFormatString())) {
								value = df.format(cell.getNumericCellValue());
							} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
								value = nf.format(cell.getNumericCellValue());
							} else {
								value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
							}
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN:
							value = cell.getBooleanCellValue();
							break;
						case XSSFCell.CELL_TYPE_BLANK:
							value = "";
							break;
						default:
							value = cell.toString();
						}
						if (value == null || "".equals(value)) {
							continue;
						}
						linked.add(value);

					}
					list.add(linked);
				}
				String userId = "";
				if (WebPageUtil.getLoginedUserId() != null) {
					userId = (WebPageUtil.getLoginedUserId());
				}
				System.out.println("==========size===========" + allModelList);
				List<ImportExcel> excelList = new ArrayList<ImportExcel>();
				List<ImportExcel> stockList = new ArrayList<ImportExcel>();
				List<ImportExcel> displayListRe = new ArrayList<ImportExcel>();

				List<ImportExcel> displayListDelete = new ArrayList<ImportExcel>();
				List<ImportExcel> stocksListByDelete = new ArrayList<ImportExcel>();
				System.out.println("error===="+msg);
				if (msg.length() <= 0) {
					for (int i = 0; i < allModelList.size(); i++) {

						if (

						allModelList.get(i).get("Customer") != null && allModelList.get(i).get("Customer") != ""

								&& allModelList.get(i).get("Model") != null && allModelList.get(i).get("Model") != ""

								&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""

								&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
								&& userId != null && userId != ""

						) {
							ImportExcel excel = new ImportExcel();
							excel.setUserId(userId);
							excel.setModel(allModelList.get(i).get("Model").toString());
							excel.setDataDate(allModelList.get(i).get("DataDate").toString());
							excel.setCustomerId(allModelList.get(i).get("Customer").toString());

							excel.setCountryId(allModelList.get(i).get("Country").toString());

							if (allModelList.get(i).get("sold") != null
									&& Integer.parseInt(allModelList.get(i).get("sold").toString()) != 0
									&& allModelList.get(i).get("Price") != null) {

								BigDecimal bd = new BigDecimal(allModelList.get(i).get("sold").toString());

								int qty = bd.intValue();
								excel.setSaleQty(bd.intValue());
								excel.setSource("PC");

								java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
								nf.setGroupingUsed(false);

								excel.setSalePrice(Double.parseDouble(
										nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString()))));

								double price = Double.parseDouble(
										nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString())));

								bd = new BigDecimal(qty * price);

								excel.setAmt(String.valueOf(bd.longValue()));
								excel.setH_qty(qty);
								if (allModelList.get(i).get("exchange") != null
										&& Float.parseFloat(allModelList.get(i).get("exchange").toString()) != 0) {

									Float exchange = Float.parseFloat(allModelList.get(i).get("exchange").toString());
									
									if (exchange != null) {
										bd = new BigDecimal(price * exchange);
										double Hprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
										excel.setH_price(Hprice);
										bd = new BigDecimal(qty * Hprice);
										BigDecimal HAmt = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
										excel.setH_amt(String.valueOf(HAmt));
									}

								}else {
									excel.setH_price(0);
									excel.setH_amt(String.valueOf('0'));
								}

								excelList.add(excel);

							} else if (allModelList.get(i).get("stocks") != null
									&& Integer.parseInt(allModelList.get(i).get("stocks").toString()) != 0) {
								excel.setStockQty(Integer.parseInt(allModelList.get(i).get("stocks").toString()));

								stocksListByDelete.add(excel);

								excel.setClassId(1);
								stockList.add(excel);

							} else if (allModelList.get(i).get("display") != null
									&& Integer.parseInt(allModelList.get(i).get("display").toString()) != 0) {
								excel.setDisPlayQty(Integer.parseInt(allModelList.get(i).get("display").toString()));

								displayListDelete.add(excel);

								excel.setClassId(1);
								displayListRe.add(excel);

							}

						}
					}

					// 鍒ゆ柇璇ユ満鍨嬭闂ㄥ簵鏄惁瀛樺湪锛屽瓨鍦ㄧ殑璇濆氨淇敼锛屼笉瀛樺湪灏辨彃鍏�
				}

				if (stocksListByDelete.size() > 0) {
					importExcelDao.deleteStockCountByRE(stocksListByDelete);
					if (stockList.size() > 0) {
						DateUtil.remove(stockList);

						importExcelDao.saveStocks(stockList);
					}
				}

				if (excelList.size() > 0) {
					int row = importExcelDao.saveSales(excelList);
					if (row > 0) {
						DateUtil.ListSort(excelList);
						String[] beginDate = excelList.get(0).getDataDate().split("-");
						String[] endDate = excelList.get(excelList.size() - 1).getDataDate().split("-");

						String beg = beginDate[0] + "-" + beginDate[1] + "-01";
						String end = endDate[0] + "-" + endDate[1] + "-31";

							/*importExcelDao.insertTVSaleVive(4+"", beg, end);
							importExcelDao.saleRe(4+"", beg, end);*/

					}
				}

				if (displayListDelete.size() > 0) {
					importExcelDao.deleteStockCountByRE(displayListDelete);
					if (displayListRe.size() > 0) {
						DateUtil.remove(displayListRe);

						importExcelDao.saveDisPlaysByRe(displayListRe);
					}
				}

				System.out.println("=========================Time" + (System.currentTimeMillis() - start));

				if (msg.length() > 0) {
					return msg.toString();
				} else {

					return "";
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg.append(e.getMessage());
				return msg.toString();
			}

		}


	@Override
	public String read2007ExcelByIn(File file, String what) throws IOException {
		Object value = null;
		String countryId=WebPageUtil.getLoginedUser().getPartyId();
		StringBuffer msg = new StringBuffer();
		String countrySale="";
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
		List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			HashMap<String, Object> modelMap = new HashMap<String, Object>();
			XSSFRow row = null;
			XSSFCell cell = null;
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			modelMap.put("Country", countryId);
			String shopName = "";
			Excel shop = null;
			if (row.getCell(3) != null && row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& !getCellValueByCell(row.getCell(3)).equals("")) {
				shopName = getCellValueByCell(row.getCell(3));
				shop = importExcelDao.getShopByNames(shopName);
				if (shop == null) {
					msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.shop") + "("
							+getCellValueByCell(row.getCell(3))+ ")" + "<br/>");
				}else {
					modelMap.put("Store", shop.getShopId());
				}

			} else {
				if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& !getCellValueByCell(row.getCell(5)).equals("") 
						) {
					msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
							+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.null") + "<br/>");
				}

			}

			SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			String dt = dfd.format(date);
			Date dt1 = dfd.parse(dt);
			Date dt2;
			String Dat="";
			if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
					&&  !getCellValueByCell(row.getCell(5)).equals("")
					) {

				if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&&  !getCellValueByCell(row.getCell(0)).equals("")) {
					try {
						format.setLenient(false);
						date = format.parse(getCellValueByCell(row.getCell(0)));// 有异常要捕获
						dfd.setLenient(false);
						String newD = dfd.format(date);
						date = dfd.parse(newD);// 有异常要捕获
						dt2 = dfd.parse(newD);
						if (dt1.getTime() < dt2.getTime()) {
							msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(1) + " " +getText("excel.error.time") + "<br/>");
						} else {
							modelMap.put("DataDate", newD);
							Dat=newD;
						}
							

					} catch (Exception e) {
						msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(1) +" "+ getText("excel.error.date") + "<br/>");
					}

				} else {
					msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
							+ DateUtil.getExcelColumnLabel(1) +" "+ getText("excel.error.dateNo") + "<br/>");
				}

			}

			if (row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& !getCellValueByCell(row.getCell(5)).equals("")
					
					) {
				String cond   = " AND t.`party_id`='"+countryId+"'";
				String modelName=getCellValueByCell(row.getCell(5));
				int model = modelMapDao.getModelIdByParty(cond, modelName, "");
				if (model >= 1) {
					modelMap.put("Model", modelName);
					
					if (row.getCell(6) != null && row.getCell(6).getCellType() != HSSFCell.CELL_TYPE_BLANK

					) {
						switch (row.getCell(6).getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:
							msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(7) +" "+ getText("excel.error.num")
									+ "<br/>");
							break;

						case HSSFCell.CELL_TYPE_FORMULA:
							try {
							FormulaEvaluator evaluator = row.getCell(6).getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				        	evaluator.evaluateFormulaCell(row.getCell(6));
				        	CellValue cellValues = evaluator.evaluate(row.getCell(6));
				        	double celldata = cellValues.getNumberValue();
				        	
							      	if (celldata != 0 && celldata!=0.0) {
							      		modelMap.put("sold", (int)celldata);
								

							}
							
						}catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
							+ DateUtil.getExcelColumnLabel(7) +"  "+ getText("excel.error.formulaError") + "<br/>");
						}
						
							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

							if (row.getCell(6).getNumericCellValue() != 0) {
								modelMap.put("sold",(int) row.getCell(6).getNumericCellValue());

							}

							break;

						case HSSFCell.CELL_TYPE_ERROR:

							break;

						}

					}
					
					
					
					if (row.getCell(9) != null && row.getCell(9).getCellType() != HSSFCell.CELL_TYPE_BLANK

							) {
								switch (row.getCell(9).getCellType()) {

								case HSSFCell.CELL_TYPE_STRING:
									msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
											+ DateUtil.getExcelColumnLabel(10)+" " + getText("excel.error.num")
											+ "<br/>");
									break;

								case HSSFCell.CELL_TYPE_FORMULA:
									try {
									FormulaEvaluator evaluator = row.getCell(9).getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
						        	evaluator.evaluateFormulaCell(row.getCell(9));
						        	CellValue cellValues = evaluator.evaluate(row.getCell(9));
						        	double celldata = cellValues.getNumberValue();
						        	modelMap.put("Price", celldata);
									      	if (celldata != 0 && celldata!=0.0) {
									      		
									      		if (countryId != null && !countryId.equals("")

														) {
															String[] days = Dat.split("-");
															String begin = days[0] + "-" + days[1] + "-01";
															String end = days[0] + "-" + days[1] + "-31";
															Float exchange = importExcelDao.selectExchange(countryId, begin, end, Dat);
															if (exchange == null) {
																msg.append(getText("excel.error.exchange") + " " + days[1] + "/" + days[0]
																		+ "<br/>");
															} else {
																modelMap.put("exchange", exchange);
															}

														}

									}
									
								}catch (Exception e) {
									msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(10) +"  "+ getText("excel.error.formulaError") + "<br/>");
								}
								
									break;

								case HSSFCell.CELL_TYPE_NUMERIC:
									modelMap.put("Price", row.getCell(9).getNumericCellValue());
									

									break;

								case HSSFCell.CELL_TYPE_ERROR:

									break;

								}

							}else {
								if (countryId != null && !countryId.equals("")) {

									Float price = importExcelDao.selectPrice(countryId,
											getCellValueByCell(row.getCell(9)));
									if (price != null && !price.equals("") && price != 0 && price != 0.0) {

										modelMap.put("Price", price);

									} else {
										modelMap.put("Price", 0);
										

									}

								}
							}
				
				}else {
					SellIn channelModel = sellInDao.selectCustomerModel(modelName, countryId, "");
					if (channelModel == null) {
						msg.append(getText("excel.error.row") + (i + 1)+" " + getText("excel.error.cell")
						+ DateUtil.getExcelColumnLabel(6)+
						" "
								+ getText("excel.error.model") + "(" + modelName + ")" + "<br/>");
					} else {
						modelMap.put("Model", modelName);
						if (row.getCell(6) != null && row.getCell(6).getCellType() != HSSFCell.CELL_TYPE_BLANK
								&& !getCellValueByCell(row.getCell(6)).equals("")
								) {
							modelMap.put("sold", getCellValueByCell(row.getCell(6)));
						}
						
						if (row.getCell(9) != null && row.getCell(9).getCellType() != HSSFCell.CELL_TYPE_BLANK
								&& !getCellValueByCell(row.getCell(9)).equals("")
								) {
							modelMap.put("Price", getCellValueByCell(row.getCell(9)));
						}else {
							if (countryId != null && !countryId.equals("")) {

								Float price = importExcelDao.selectPrice(countryId,
										getCellValueByCell(row.getCell(9)));
								if (price != null && !price.equals("") && price != 0 && price != 0.0) {

									modelMap.put("Price", price);

								} else {
									modelMap.put("Price", 0);
									

								}

							}
						}
						
					}
				}
			}else {
				msg.append(getText("excel.error.row") + (i + 1) +" "+ getText("excel.error.cell")
				+ DateUtil.getExcelColumnLabel(6)+
				" "
						+ getText("excel.error.null") + "<br/>");

			
			}
			allModelList.add(modelMap);

			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

				if (j >= 0) {
					cell = row.getCell(j);
				}

				if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number
															// String 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);

			}
			list.add(linked);
		}
		String userId = "";
		if (WebPageUtil.getLoginedUserId() != null) {
			userId = (WebPageUtil.getLoginedUserId());
		}
		System.out.println("==========size===========" + allModelList);
		List<ImportExcel> excelList = new ArrayList<ImportExcel>();
		List<ImportExcel> stockList = new ArrayList<ImportExcel>();
		List<ImportExcel> displayListRe = new ArrayList<ImportExcel>();

		List<ImportExcel> displayListDelete = new ArrayList<ImportExcel>();
		List<ImportExcel> stocksListByDelete = new ArrayList<ImportExcel>();
		if (msg.length() <= 0) {
			for (int i = 0; i < allModelList.size(); i++) {

				if (

				allModelList.get(i).get("Store") != null && allModelList.get(i).get("Store") != ""

						&& allModelList.get(i).get("Model") != null && allModelList.get(i).get("Model") != ""

						&& allModelList.get(i).get("Country") != null && allModelList.get(i).get("Country") != ""

						&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
						&& userId != null && userId != ""

				) {
					ImportExcel excel = new ImportExcel();
					excel.setUserId(userId);
					excel.setModel(allModelList.get(i).get("Model").toString());
					excel.setDataDate(allModelList.get(i).get("DataDate").toString());
					excel.setShopId(Integer.parseInt(allModelList.get(i).get("Store").toString()));

					excel.setCountryId(allModelList.get(i).get("Country").toString());

					if (allModelList.get(i).get("sold") != null
							&& Integer.parseInt(allModelList.get(i).get("sold").toString()) != 0
							&& allModelList.get(i).get("Price") != null) {

						BigDecimal bd = new BigDecimal(allModelList.get(i).get("sold").toString());

						int qty = bd.intValue();
						excel.setSaleQty(bd.intValue());
						excel.setSource("PC");

						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
						nf.setGroupingUsed(false);

						excel.setSalePrice(Double.parseDouble(
								nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString()))));

						double price = Double.parseDouble(
								nf.format(Double.parseDouble(allModelList.get(i).get("Price").toString())));

						bd = new BigDecimal(qty * price);

						excel.setAmt(String.valueOf(bd.longValue()));
						excel.setH_qty(qty);
						if (allModelList.get(i).get("exchange") != null
								&& Float.parseFloat(allModelList.get(i).get("exchange").toString()) != 0) {

							Float exchange = Float.parseFloat(allModelList.get(i).get("exchange").toString());
						
							if (exchange != null) {
								bd = new BigDecimal(price * exchange);
								double Hprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								excel.setH_price(Hprice);
								bd = new BigDecimal(qty * Hprice);
								BigDecimal HAmt = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
								excel.setH_amt(String.valueOf(HAmt));
							}

						}else {
							excel.setH_price(0);
							excel.setH_amt(String.valueOf('0'));
						}

						excelList.add(excel);

					} 

				}
			}

			// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
		}

		

		if (excelList.size() > 0) {
			int row = importExcelDao.saveSales(excelList);
			if (row > 0) {
				DateUtil.ListSort(excelList);
				String[] beginDate = excelList.get(0).getDataDate().split("-");
				String[] endDate = excelList.get(excelList.size() - 1).getDataDate().split("-");

				String beg = beginDate[0] + "-" + beginDate[1] + "-01";
				String end = endDate[0] + "-" + endDate[1] + "-31";
				String countryList = "";

				if (what.equals("shop")) {
					importExcelDao.insertTVSaleVive(countrySale, beg, end);
					importExcelDao.insertACSaleVive(countrySale, beg, end);
					importExcelDao.saleRe(countrySale, beg, end);
					importExcelDao.updateStocksByShop(excelList);
				}

			}
		}

		
		if (msg.length() > 0) {
			return msg.toString();
		} else {

			return "";
		}

	} catch (Exception e) {
		e.printStackTrace();
		msg.append(e.getMessage());
		return msg.toString();
	}

	}

	
	 
}
