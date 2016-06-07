package com.typartner.find.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * http或者https网络通信客户端<br/>
 * ========================================================================<br/>
 * api说明：<br/>
 * setReqContent($reqContent),设置请求内容，无论post和get，都用get方式提供<br/>
 * getResContent(), 获取应答内容<br/>
 * setMethod(method),设置请求方法,post或者get<br/>
 * getErrInfo(),获取错误信息<br/>
 * setCertInfo(certFile, certPasswd),设置证书，双向https时需要使用<br/>
 * setCaInfo(caFile), 设置CA，格式未pem，不设置则不检查<br/>
 * setTimeOut(timeOut)， 设置超时时间，单位秒<br/>
 * getResponseCode(), 取返回的http状态码<br/>
 * call(),真正调用接口<br/>
 * getCharset()/setCharset(),字符集编码<br/>
 * 
 * ========================================================================<br/>
 * 
 */
public class HttpClientUtil {

	public static final String SunX509 = "SunX509";
	public static final String JKS = "JKS";
	public static final String PKCS12 = "PKCS12";
	public static final String TLS = "TLS";

	private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";

	private String contentType;

	/** 请求地址 */
	private String reqURL;

	/** 请求内容，无论post和get，都用get方式提供 */
	private String reqContent;

	/** 应答内容 */
	private String resContent;

	/** 请求方法 */
	private String method;

	/** 错误信息 */
	private String errInfo;

	/** 连超时时间,以秒为单位 */
	private int timeOut;

	/** 读取超时时间,以秒为单位 */
	private int readTimeOut;

	/** http应答编码 */
	private int responseCode;

	/** 字符编码 */
	private String charset;

	private InputStream inputStream;

	private HttpURLConnection httpConn;

	private HttpsURLConnection httpsConn;

	/** https需要参数 */

	/** ca证书文件 */
	private File caFile;

	/** 证书文件 */
	private File certFile;

	private String caPasswd;
	/** 证书密码 */
	private String certPasswd;

	public HttpClientUtil() {
		this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
		this.reqURL = "";
		this.reqContent = "";
		this.resContent = "";
		this.method = "POST";
		this.errInfo = "";
		this.timeOut = 30;// 30秒
		this.readTimeOut = 60;// 60秒

		this.responseCode = 0;
		this.charset = "UTF-8";
		this.httpConn = null;
		this.httpsConn = null;
		this.inputStream = null;

		this.caFile = null;
		this.certFile = null;
		this.caPasswd = "";
		this.certPasswd = "";
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getReqURL() {
		return reqURL;
	}

	public void setReqURL(String reqURL) {
		this.reqURL = reqURL;
	}

	/**
	 * 设置请求内容
	 * 
	 * @param reqContent
	 *            表求内容
	 */
	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}

	/**
	 * 获取结果内容
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String getResContent() {
		try {
			this.doResponse();
		} catch (IOException e) {
			this.errInfo = e.getMessage();
			// return "";
		}
		return this.resContent;
	}

	/**
	 * 从返回流中读取对象
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object getResObject() throws IOException, ClassNotFoundException {
		if (null == this.inputStream) {
			return null;
		}

		Object obj = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(this.inputStream);
			obj = ois.readObject();
		} finally {
			// 关闭输入流
			ois.close();
			this.inputStream.close();
		}
		return obj;
	}

	/**
	 * 设置请求方法post或者get
	 * 
	 * @param method
	 *            请求方法post/get
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return String
	 */
	public String getErrInfo() {
		return this.errInfo;
	}

	public HttpURLConnection getHttpConn() {
		if (this.httpConn == null) {
			try {
				this.httpConn = getHttpURLConnection(this.reqURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.httpConn;
	}

	public HttpsURLConnection getHttpsConn() {
		if (this.httpsConn == null) {
			try {
				this.httpsConn = getHttpsURLConnection(this.reqURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.httpsConn;
	}

	/**
	 * 设置超时时间,以秒为单位
	 * 
	 * @param timeOut
	 *            超时时间,以秒为单位
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public void setCaFile(File caFile) {
		this.caFile = caFile;
	}

	public void setCertFile(File certFile) {
		this.certFile = certFile;
	}

	public void setCaPasswd(String caPasswd) {
		this.caPasswd = caPasswd;
	}

	public void setCertPasswd(String certPasswd) {
		this.certPasswd = certPasswd;
	}

	/**
	 * 设置读取超时时间,以秒为单位
	 * 
	 * @param readTimeOut
	 *            超时时间,以秒为单位
	 */
	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	/**
	 * 获取http状态码
	 * 
	 * @return int
	 */
	public int getResponseCode() {
		return this.responseCode;
	}

	public void callHttp() throws IOException {
		String url = this.reqURL;
		String queryString = this.reqContent;

		if (this.httpConn == null) {
			this.httpConn = this.getHttpURLConnection(url);
		}

		if ("POST".equals(this.method.toUpperCase())) {
			byte[] postData = queryString.getBytes(this.charset);
			this.httpPostMethod(url, postData);

			return;
		}

		this.httpGetMethod(this.reqContent);

	}

	public void callHttps() throws IOException, CertificateException,
			KeyStoreException, NoSuchAlgorithmException,
			UnrecoverableKeyException, KeyManagementException {
		String url = this.reqURL;
		String queryString = this.reqContent;

		if (this.httpsConn == null)
			this.httpsConn = this.getHttpsURLConnection(url);

		FileInputStream trustStream = new FileInputStream(caFile);
		FileInputStream keyStream = new FileInputStream(certFile);

		SSLContext sslContext = getSSLContext(trustStream, caPasswd, keyStream,
				certPasswd);

		// 关闭流
		keyStream.close();
		trustStream.close();

		if ("POST".equals(this.method.toUpperCase())) {
			byte[] postData = queryString.getBytes(this.charset);
			this.httpsPostMethod(url, postData, sslContext);

			return;
		}

		this.httpsGetMethod(this.reqContent, sslContext);

	}

	/**
	 * 以http post方式通信
	 * 
	 * @param url
	 * @param postData
	 * @throws IOException
	 */
	protected void httpPostMethod(String url, byte[] postData)
			throws IOException {

		this.doPost(this.httpConn, postData);
	}

	/**
	 * 以http get方式通信
	 * 
	 * @param url
	 * @throws IOException
	 */
	protected void httpGetMethod(String url) throws IOException {

		this.setHttpRequest(this.httpConn);

		this.httpConn.setRequestMethod("GET");

		this.responseCode = this.httpConn.getResponseCode();

		this.inputStream = this.httpConn.getInputStream();

	}

	/**
	 * 以https get方式通信
	 * 
	 * @param url
	 * @param sslContext
	 * @throws IOException
	 */
	protected void httpsGetMethod(String url, SSLContext sslContext)
			throws IOException {

		SSLSocketFactory sf = sslContext.getSocketFactory();

		this.httpsConn.setSSLSocketFactory(sf);

		this.doGet(httpsConn);

	}

	protected void httpsPostMethod(String url, byte[] postData,
			SSLContext sslContext) throws IOException {

		SSLSocketFactory sf = sslContext.getSocketFactory();

		this.httpsConn.setSSLSocketFactory(sf);

		this.doPost(this.httpsConn, postData);

	}

	/**
	 * 设置http请求默认属性
	 * 
	 * @param httpConnection
	 */
	protected void setHttpRequest(HttpURLConnection httpConnection) {

		// 设置连接超时时间
		httpConnection.setConnectTimeout(this.timeOut * 1000);

		// 设置读取超时时间
		httpConnection.setReadTimeout(this.readTimeOut * 1000);

		// Content-Type
		httpConnection.setRequestProperty("Content-Type", this.contentType);

		// User-Agent
		httpConnection.setRequestProperty("User-Agent",
				HttpClientUtil.USER_AGENT_VALUE);

		// 不使用缓存
		httpConnection.setUseCaches(false);

		// 允许输入输出
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);

	}

	/**
	 * 处理应答
	 * 
	 * @throws IOException
	 */
	protected void doResponse() throws IOException {

		if (null == this.inputStream) {
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.inputStream, this.charset));

		// 获取应答内容
		this.resContent = this.bufferedReader2String(reader);

		// 关闭流
		reader.close();

		// 关闭输入流
		this.inputStream.close();

	}

	/**
	 * post方式处理
	 * 
	 * @param conn
	 * @param postData
	 * @throws IOException
	 */
	protected void doPost(HttpURLConnection conn, byte[] postData)
			throws IOException {

		// 以post方式通信
		conn.setRequestMethod("POST");

		// 设置请求默认属性
		this.setHttpRequest(conn);

		// Content-Type
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");

		BufferedOutputStream out = new BufferedOutputStream(
				conn.getOutputStream());

		final int len = 1024; // 1KB
		this.doOutput(out, postData, len);

		// 关闭流
		out.close();

		// 获取响应返回状态码
		this.responseCode = conn.getResponseCode();

		// 获取应答输入流
		this.inputStream = conn.getInputStream();

	}

	/**
	 * get方式处理
	 * 
	 * @param conn
	 * @throws IOException
	 */
	protected void doGet(HttpURLConnection conn) throws IOException {

		// 以GET方式通信
		conn.setRequestMethod("GET");

		// 设置请求默认属性
		this.setHttpRequest(conn);

		// 获取响应返回状态码
		this.responseCode = conn.getResponseCode();

		// 获取应答输入流
		this.inputStream = conn.getInputStream();
	}

	/**
	 * get HttpURLConnection
	 * 
	 * @param strUrl
	 *            url地址
	 * @return HttpURLConnection
	 * @throws IOException
	 */
	public HttpURLConnection getHttpURLConnection(String strUrl)
			throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		return httpURLConnection;
	}

	/**
	 * get HttpsURLConnection
	 * 
	 * @param strUrl
	 *            url地址
	 * @return HttpsURLConnection
	 * @throws IOException
	 */
	public HttpsURLConnection getHttpsURLConnection(String strUrl)
			throws IOException {
		URL url = new URL(strUrl);
		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
				.openConnection();
		return httpsURLConnection;
	}

	/**
	 * 查询字符串转换成Map<br/>
	 * name1=key1&name2=key2&...
	 * 
	 * @param queryString
	 * @return
	 */
	public Map<String, String> queryString2Map(String queryString) {
		if (null == queryString || "".equals(queryString)) {
			return null;
		}

		Map<String, String> m = new HashMap<String, String>();
		String[] strArray = queryString.split("&");
		for (int index = 0; index < strArray.length; index++) {
			String pair = strArray[index];
			this.putMapByPair(pair, m);
		}

		return m;

	}

	/**
	 * 把键值添加至Map<br/>
	 * pair:name=value
	 * 
	 * @param pair
	 *            name=value
	 * @param m
	 */
	public void putMapByPair(String pair, Map<String, String> m) {

		if (null == pair || "".equals(pair)) {
			return;
		}

		int indexOf = pair.indexOf("=");
		if (-1 != indexOf) {
			String k = pair.substring(0, indexOf);
			String v = pair.substring(indexOf + 1, pair.length());
			if (null != k && !"".equals(k)) {
				m.put(k, v);
			}
		} else {
			m.put(pair, "");
		}
	}

	/**
	 * BufferedReader转换成String<br/>
	 * 注意:流关闭需要自行处理
	 * 
	 * @param reader
	 * @return String
	 * @throws IOException
	 */
	public String bufferedReader2String(BufferedReader reader)
			throws IOException {
		StringBuffer buf = new StringBuffer();
		String line = null;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			buf.append(line);
			buf.append("\n");
			i++;
		}
		if (i > 0) {
			return buf.deleteCharAt(buf.length() - 1).toString();
		}
		return buf.toString();
	}

	/**
	 * 处理输出<br/>
	 * 注意:流关闭需要自行处理
	 * 
	 * @param out
	 * @param data
	 * @param len
	 * @throws IOException
	 */
	public void doOutput(OutputStream out, byte[] data, int len)
			throws IOException {
		int dataLen = data.length;
		int off = 0;
		while (off < dataLen) {
			if (len >= dataLen) {
				out.write(data, off, dataLen);
			} else {
				out.write(data, off, len);
			}

			// 刷新缓冲区
			out.flush();

			off += len;

			dataLen -= len;
		}

	}

	public InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

	/**
	 * 字符串转换成char数组
	 * 
	 * @param str
	 * @return char[]
	 */
	public char[] str2CharArray(String str) {
		if (null == str)
			return null;

		return str.toCharArray();
	}

	/**
	 * 获取SSLContext
	 * 
	 * @param trustFile
	 * @param trustPasswd
	 * @param keyFile
	 * @param keyPasswd
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	public SSLContext getSSLContext(FileInputStream trustFileInputStream,
			String trustPasswd, FileInputStream keyFileInputStream,
			String keyPasswd) throws NoSuchAlgorithmException,
			KeyStoreException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException {

		// ca
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(SunX509);
		KeyStore trustKeyStore = KeyStore.getInstance(JKS);
		trustKeyStore.load(trustFileInputStream, str2CharArray(trustPasswd));
		tmf.init(trustKeyStore);

		final char[] kp = str2CharArray(keyPasswd);
		KeyManagerFactory kmf = KeyManagerFactory
				.getInstance(HttpClientUtil.SunX509);
		KeyStore ks = KeyStore.getInstance(PKCS12);
		ks.load(keyFileInputStream, kp);
		kmf.init(ks, kp);

		SecureRandom rand = new SecureRandom();
		SSLContext ctx = SSLContext.getInstance(TLS);
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

		return ctx;
	}

	/**
	 * 获取CA证书信息
	 * 
	 * @param cafile
	 *            CA证书文件
	 * @return Certificate
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static Certificate getCertificate(File cafile)
			throws CertificateException, IOException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(cafile);
		Certificate cert = cf.generateCertificate(in);
		in.close();
		return cert;
	}
}
