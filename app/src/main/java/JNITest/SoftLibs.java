package JNITest;


public class SoftLibs {

	private static SoftLibs jni = new SoftLibs();

	private SoftLibs() {
		System.loadLibrary("SoftCpt");
	}

	public static SoftLibs getInstance() {
		return jni;
	}

	// softlib-> SymEncrypt,support des-ECB 3DES and SM4
	public native byte[] SymEncrypt(int algorithm, String key, byte[] plainData);

	// softlib-> SymDecrypt,support 3DES-ECB and SM4
	public native byte[] SymDecrypt(int algorithm, String key,
			byte[] inputCipher);

	// softlib-> DecryptByPriKey,support RSA SM2
	public native byte[] DecryptByPriKey(int algorithm, String Prikey,
			byte[] plainData);

	// softlib-> EncryptByPubkey,support RSA SM2
	public native byte[] EncryptByPubkey(int algorithm, String Pubkey,
			byte[] inputCipher);

	// softlib-> SignByPriKey,support RSA SM2
	public native byte[] SignByPriKey(int algorithm, String Prikey,
			byte[] plainData);

	// softlib-> VerifyByPubkey,support RSA SM2
	public native boolean VerifyByPubkey(int algorithm, String Pubkey,
			byte[] plainData, byte[] signedData);

	// softlib-> GenKeyPair,gen keypair,get pubkey, prikey encypted with Pin
	// ,support RSA SM2
	public native byte[] GenKeyPair(int algorithm, String ID, String Pin);

	// softlib-> ApplyCert,apply cert From CA,support RSA SM2
	public native boolean ApplyCert(int algorithm, String ID, // user ID
			String pubkey, // user pubkey base64
			String ST, // USER province
			String L, // USER city
			String O, // USER company
			String OU, // USER department
			String Email, // USER email
			String certType, // single cert or double cert
			String notBefore, // cert begin time
			String notAfter, // cert finish time
			String extend); // cert extend info

	// softlib-> ReadLocalCert
	// ID�� user id
	// strCertUsage�� CertUsage, include signature cert and encrypt cert
	// #define SGD_KEYUSAGE_SIGN 0x00020401 ǩ��֤��
	// #define SGD_KEYUSAGE_KEYEXCHANGE 0x00020402 ����֤��
	public native String ReadLocalCert(String ID, int strCertUsage);

	// softlib-> VerifyLocalCert
	// ID�� user id
	// strCertUsage�� CertUsage, include signature cert and encrypt cert
	// #define SGD_KEYUSAGE_SIGN 0x00020401 ǩ��֤��
	// #define SGD_KEYUSAGE_KEYEXCHANGE 0x00020402 ����֤��
	// CrlData:CRL data down load from CA
	public native boolean VerifyLocalCert(String ID, int strCertUsage,
			String CrlData);

	// softlib-> VerifyDevieAndLocalCert
	// ID�� user id
	// strCertUsage�� CertUsage, include signature cert and encrypt cert
	// #define SGD_KEYUSAGE_SIGN 0x00020401 ǩ��֤��
	// #define SGD_KEYUSAGE_KEYEXCHANGE 0x00020402 ����֤��
	// deviceID:device ID��can be imei.
	public native boolean VerifyDevieAndLocalCert(String ID, int strCertUsage,
			String deviceID);

	// softlib-> VerifyCertByIssuerCert
	// CertData:User cert
	// IssuerCertData:Issuer Cert
	public native boolean VerifyCertByIssuerCert(String CertData,
			String IssuerCertData);
	
	// softlib-> deleteLocalData
	// ID�� user id
	public native boolean deleteLocalData(String ID);	
	
	// softlib-> revokeCert
	// CertData:User cert
	public native boolean revokeCert(String CertData);	
	
}
