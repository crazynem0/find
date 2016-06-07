package com.typartner.find.common.util;

import java.util.Collection;
import java.util.LinkedList;

/**
 * 计算分页控制器
 * @author zhangli
 * @version 1.0
 *
 */

public class PageController {

    protected int currentPage = 1;                            //当前页码
    protected int totalPages = 0;                             //总页数
    protected int pageSize =10;                               //每页行数
    protected int totalRows = 0;                              //总行数
    protected int pageStartRow = currentPage * pageSize;      //当前页开始行
    protected int pageEndRow = currentPage * (pageSize + 1);  //当前页结束行
    protected boolean hasNextPage = false;                    //是否有下一页
    protected boolean hasPreviousPage = false;                //是否有上一页
    private int previousPage ,nextPage;                       //上一页数，下一页数
    private String querySqlTemp = null;                       //
    Collection allPageNum = new LinkedList();
    private String cookie;

    /**
     * ������
     */
    public PageController() {
    }

    /**
     * @param name: ��ҳ
	 * @return ���ؿ�
	 */
    public void flip() {

        totalPages = (totalRows - 1) / pageSize + 1;
        pageStartRow = pageSize * (currentPage - 1) + 1;
        pageEndRow = pageSize * currentPage;
        if (currentPage > 1) {
            hasPreviousPage = true;
        }
        if (currentPage < totalPages) {
            hasNextPage = true;
        }
    }

    /**
	 * ��ݻ���ܼ�¼������ҳ�ĸ����ȡֵ
	 */
    public void update() {
        
    	totalPages = (totalRows - 1) / pageSize + 1;
        for (int i = 0; i < totalPages; i++) {
            allPageNum.add(String.valueOf(i + 1));
        }
        pageStartRow = pageSize * (currentPage - 1);
        pageEndRow = pageStartRow + pageSize;
        if ((currentPage > 1) && (totalPages > 1)) {
            hasPreviousPage = true;
            previousPage = currentPage -1;
        }
        if ((currentPage < totalPages) && (totalPages >1)) {
            hasNextPage = true;
            nextPage = currentPage + 1;
        }
    }
    
    /**
	 * 
	 */ 
   public String getLinkStr() {

        StringBuffer sb = new StringBuffer();
        sb.append("<< ");
        sb.append("");
        sb.append(" >>");

        return sb.toString();
    }

    /**
	 * @return ���ص�ǰҳ
	 */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage: ��ǰҳ
	 * @return ���ؿ�
	 */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
	 * @return �����Ƿ�����һҳ
	 */
    public boolean isHasNextPage() {
        return hasNextPage;
    }

    /**
     * @param hasNextPage: �Ƿ�����һҳ
	 * @return ���ؿ�
	 */
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
	 * @return �����Ƿ�����һҳ
	 */
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    /**
     * @param hasPreviousPage: �Ƿ�����һҳ
	 * @return ���ؿ�
	 */
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
    
    /**
	 * @return ����ÿҳ��ʾ��ݵ���ֹ��
	 */
    public int getPageEndRow() {
        return pageEndRow;
    }

    /**
     * @param pageEndRow: ÿҳ��ʾ��ݵ���ֹ��
	 * @return ���ؿ�
	 */
    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    /**
	 * @return ����ÿҳ��ʾ�����
	 */
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * @param pageSize: ÿҳ��ʾ�����
	 * @return ���ؿ�
	 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
	 * @return ����ÿҳ����ʼ��
	 */
    public int getPageStartRow() {
        return pageStartRow;
    }

    /**
     * @param pageStartRow: ÿҳ����ʼ��
	 * @return ���ؿ�
	 */
    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    /**
	 * @return ������ҳ��
	 */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages: ��ҳ��
	 * @return ���ؿ�
	 */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
	 * @return �����ܼ�¼��
	 */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @param totalRows: �ܼ�¼��
	 * @return ���ؿ�
	 */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    /**
	 * @return ������ҳ��
	 */
    public Collection getAllPageNum() {
        return allPageNum;
    }
    
    /**
     * @param allPageNum: ��ҳ��
	 * @return ���ؿ�
	 */
    public void setAllPageNum(Collection allPageNum) {
        this.allPageNum = allPageNum;
    }
    
    /**
	 * @return ������һҳ��
	 */
    public int getNextPage() {
        return nextPage;
    }
    
    /**
     * @param nextPage: ��һҳ��
	 * @return ���ؿ�
	 */
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    
    /**
	 * @return ������һҳ��
	 */
    public int getPreviousPage() {
        return previousPage;
    }
    
    /**
     * @param previousPage: ��һҳ��
	 * @return ���ؿ�
	 */
    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }
    
    /**
	 * @return ���ز�ѯ���
	 */
    public String getQuerySqlTemp() {
        return querySqlTemp;
    }
    
    /**
     * @param querySqlTemp: ��ѯ���
	 * @return ���ؿ�
	 */
    public void setQuerySqlTemp(String querySqlTemp) {
        this.querySqlTemp = querySqlTemp;
    }

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

   
}
