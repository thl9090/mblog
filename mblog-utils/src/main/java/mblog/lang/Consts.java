/**
 * 
 */
package mblog.lang;


/**
 * @author langhsu
 *
 */
public interface Consts {
	/**
	 * 默认头像
	 */
	String AVATAR = "/assets/images/ava/default.png";
	
	/**
	 * 文件目录步进
	 */
	int FILE_PATH_SEED = 997;
	
	String SEPARATOR = ",";

	int IDENTITY_STEP = 1;
	
	int TIME_MIN = 1000;
	
	/* 状态 */
	int STATUS_NORMAL = 0;
	
	int STATUS_FEATURED = 1;
	
	int STATUS_LOCKED = 1;
	
	interface order {
		String FEATURED = "featured";
		String NEWEST = "newest";
		String HOTTEST = "hottest";
	}
	
	/**
	 * 附件-存储-本地
	 */
	int ATTACH_STORE_LOCAL = 0;

	/**
	 * 附件-存储-网络
	 */
	int ATTACH_STORE_NETWORK = 1;

	String SYSTEM_VERSION = "system.version";
}
