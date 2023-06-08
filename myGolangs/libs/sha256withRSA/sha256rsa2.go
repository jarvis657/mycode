package main

import (
	"bytes"
	"crypto"
	"crypto/md5"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/asn1"
	"encoding/base64"
	"encoding/hex"
	"encoding/pem"
	"errors"
	"fmt"
	"strings"
)

type Rsa struct {
	privateKey    string
	publicKey     string
	rsaPrivateKey *rsa.PrivateKey
	rsaPublicKey  *rsa.PublicKey
}

func NewRsa(publicKey, privateKey string) *Rsa {
	rsaObj := &Rsa{
		privateKey: privateKey,
		publicKey:  publicKey,
	}

	rsaObj.init()
	return rsaObj
}

func (this *Rsa) init() {
	if this.privateKey != "" {
		// pkcs1
		if strings.Index(string(FormatPKCS1PrivateKey(this.privateKey)), "BEGIN RSA") > 0 {
			block, _ := pem.Decode([]byte(FormatPKCS1PrivateKey(this.privateKey)))
			this.rsaPrivateKey, _ = x509.ParsePKCS1PrivateKey(block.Bytes)
		}
		if this.rsaPrivateKey == nil { //pkcs8
			block, _ := pem.Decode([]byte(FormatPKCS8PrivateKey(this.privateKey)))
			privateKey, _ := x509.ParsePKCS8PrivateKey(block.Bytes)
			this.rsaPrivateKey = privateKey.(*rsa.PrivateKey)
		}
	}
	if this.publicKey != "" {
		block, _ := pem.Decode(FormatPublicKey(this.publicKey))
		publickKey, _ := x509.ParsePKIXPublicKey(block.Bytes)
		this.rsaPublicKey = publickKey.(*rsa.PublicKey)
	}
}

/**
 * 加密
 */
func (this *Rsa) Encrypt(data []byte) ([]byte, error) {
	blockLength := this.rsaPublicKey.N.BitLen()/8 - 11
	if len(data) <= blockLength {
		return rsa.EncryptPKCS1v15(rand.Reader, this.rsaPublicKey, []byte(data))
	}

	buffer := bytes.NewBufferString("")

	pages := len(data) / blockLength

	for index := 0; index <= pages; index++ {
		start := index * blockLength
		end := (index + 1) * blockLength
		if index == pages {
			if start == len(data) {
				continue
			}
			end = len(data)
		}

		chunk, err := rsa.EncryptPKCS1v15(rand.Reader, this.rsaPublicKey, data[start:end])
		if err != nil {
			return nil, err
		}
		buffer.Write(chunk)
	}
	return buffer.Bytes(), nil
}

/**
 * 解密
 */
func (this *Rsa) Decrypt(secretData []byte) ([]byte, error) {
	blockLength := this.rsaPublicKey.N.BitLen() / 8
	if len(secretData) <= blockLength {
		return rsa.DecryptPKCS1v15(rand.Reader, this.rsaPrivateKey, secretData)
	}

	buffer := bytes.NewBufferString("")

	pages := len(secretData) / blockLength
	for index := 0; index <= pages; index++ {
		start := index * blockLength
		end := (index + 1) * blockLength
		if index == pages {
			if start == len(secretData) {
				continue
			}
			end = len(secretData)
		}

		chunk, err := rsa.DecryptPKCS1v15(rand.Reader, this.rsaPrivateKey, secretData[start:end])
		if err != nil {
			return nil, err
		}
		buffer.Write(chunk)
	}
	return buffer.Bytes(), nil
}

/**
 * 签名
 */
func (this *Rsa) Sign(data string, algorithmSign crypto.Hash) (string, error) {
	hash := algorithmSign.New()
	hash.Write([]byte(data))
	sign, err := rsa.SignPKCS1v15(rand.Reader, this.rsaPrivateKey, algorithmSign, hash.Sum(nil))
	if err != nil {
		return "", err
	}
	toString := base64.StdEncoding.EncodeToString(sign)
	return toString, err
}

/**
 * 验签
 */
func (this *Rsa) Verify(data, sign string, algorithmSign crypto.Hash) bool {
	h := algorithmSign.New()
	h.Write([]byte(data))
	bs, err := base64.StdEncoding.DecodeString(sign)
	if err != nil {
		return false
	}
	err = rsa.VerifyPKCS1v15(this.rsaPublicKey, algorithmSign, h.Sum(nil), bs)
	b := err == nil
	return b
}

/**
 * 生成pkcs1格式公钥私钥
 */
func (this *Rsa) CreateKeys(keyLength int) (privateKey, publicKey string) {
	rsaPrivateKey, err := rsa.GenerateKey(rand.Reader, keyLength)
	if err != nil {
		return
	}

	privateKey = string(pem.EncodeToMemory(&pem.Block{
		Type:  "RSA PRIVATE KEY",
		Bytes: x509.MarshalPKCS1PrivateKey(rsaPrivateKey),
	}))

	derPkix, err := x509.MarshalPKIXPublicKey(&rsaPrivateKey.PublicKey)
	if err != nil {
		return
	}

	publicKey = string(pem.EncodeToMemory(&pem.Block{
		Type:  "PUBLIC KEY",
		Bytes: derPkix,
	}))
	return
}

/**
 * 生成pkcs8格式公钥私钥
 */
func (this *Rsa) CreatePkcs8Keys(keyLength int) (privateKey, publicKey string) {
	rsaPrivateKey, err := rsa.GenerateKey(rand.Reader, keyLength)
	if err != nil {
		return
	}

	privateKey = string(pem.EncodeToMemory(&pem.Block{
		Type:  "PRIVATE KEY",
		Bytes: this.MarshalPKCS8PrivateKey(rsaPrivateKey),
	}))

	derPkix, err := x509.MarshalPKIXPublicKey(&rsaPrivateKey.PublicKey)
	if err != nil {
		return
	}

	publicKey = string(pem.EncodeToMemory(&pem.Block{
		Type:  "PUBLIC KEY",
		Bytes: derPkix,
	}))
	return
}

func (this *Rsa) MarshalPKCS8PrivateKey(key *rsa.PrivateKey) []byte {
	info := struct {
		Version             int
		PrivateKeyAlgorithm []asn1.ObjectIdentifier
		PrivateKey          []byte
	}{}
	info.Version = 0
	info.PrivateKeyAlgorithm = make([]asn1.ObjectIdentifier, 1)
	info.PrivateKeyAlgorithm[0] = asn1.ObjectIdentifier{1, 2, 840, 113549, 1, 1, 1}
	info.PrivateKey = x509.MarshalPKCS1PrivateKey(key)
	k, _ := asn1.Marshal(info)
	return k
}

const (
	PublicKeyPrefix = "-----BEGIN PUBLIC KEY-----"
	PublicKeySuffix = "-----END PUBLIC KEY-----"

	PKCS1Prefix = "-----BEGIN RSA PRIVATE KEY-----"
	PKCS1Suffix = "-----END RSA PRIVATE KEY-----"

	PKCS8Prefix = "-----BEGIN PRIVATE KEY-----"
	PKCS8Suffix = "-----END PRIVATE KEY-----"

	PublicKeyType     = "PUBLIC KEY"
	PrivateKeyType    = "PRIVATE KEY"
	RSAPrivateKeyType = "RSA PRIVATE KEY"
)

var (
	ErrLoadPrivateKey  = errors.New("xpay: private key failed to load")
	ErrLoadPublicKey   = errors.New("xpay: public key failed to load")
	ErrLoadCertificate = errors.New("xpay: certificate  failed to load")
)

func ParsePKCS1PrivateKey(data []byte) (key *rsa.PrivateKey, err error) {
	var block *pem.Block
	block, _ = pem.Decode(data)
	if block == nil {
		return nil, ErrLoadPrivateKey
	}
	key, err = x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		return nil, err
	}

	return key, err
}

func ParseCertificate(b []byte) (*x509.Certificate, error) {
	block, _ := pem.Decode(b)
	if block == nil {
		return nil, ErrLoadCertificate
	}
	csr, err := x509.ParseCertificate(block.Bytes)
	if err != nil {
		return nil, err
	}
	return csr, nil
}

func GetCertSN(cert *x509.Certificate) string {
	var value = md5.Sum([]byte(cert.Issuer.String() + cert.SerialNumber.String()))
	return hex.EncodeToString(value[:])
}

func RSAVerifyWithKey(ciphertext, sign []byte, key *rsa.PublicKey, hash crypto.Hash) error {
	var h = hash.New()
	h.Write(ciphertext)
	var hashed = h.Sum(nil)
	return rsa.VerifyPKCS1v15(key, hash, hashed, sign)
}

func FormatPublicKey(raw string) []byte {
	return formatKey(raw, PublicKeyPrefix, PublicKeySuffix, 64)
}

func FormatPKCS1PrivateKey(raw string) []byte {
	raw = strings.Replace(raw, PKCS8Prefix, "", 1)
	raw = strings.Replace(raw, PKCS8Suffix, "", 1)
	return formatKey(raw, PKCS1Prefix, PKCS1Suffix, 64)
}

func FormatPKCS8PrivateKey(raw string) []byte {
	raw = strings.Replace(raw, PKCS1Prefix, "", 1)
	raw = strings.Replace(raw, PKCS1Suffix, "", 1)
	return formatKey(raw, PKCS8Prefix, PKCS8Suffix, 64)
}

func ParsePKCS8PrivateKey(data []byte) (key *rsa.PrivateKey, err error) {
	var block *pem.Block
	block, _ = pem.Decode(data)
	if block == nil {
		return nil, ErrLoadPrivateKey
	}
	rawKey, err := x509.ParsePKCS8PrivateKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	key, ok := rawKey.(*rsa.PrivateKey)
	if !ok {
		return nil, ErrLoadPrivateKey
	}
	return key, err
}

func ParsePublicKey(data []byte) (key *rsa.PublicKey, err error) {
	var block *pem.Block
	block, _ = pem.Decode(data)
	if block == nil {
		return nil, ErrLoadPublicKey
	}
	var pubInterface interface{}
	pubInterface, err = x509.ParsePKIXPublicKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	key, ok := pubInterface.(*rsa.PublicKey)
	if !ok {
		return nil, ErrLoadPublicKey
	}

	return key, err
}

func RSASignWithKey(plaintext []byte, key *rsa.PrivateKey, hash crypto.Hash) ([]byte, error) {
	var h = hash.New()
	h.Write(plaintext)
	var hashed = h.Sum(nil)
	return rsa.SignPKCS1v15(rand.Reader, key, hash, hashed)
}

const LineBreak = "\n"

func formatKey(raw, prefix, suffix string, lineCount int) []byte {
	raw = strings.Replace(raw, prefix, "", 1)
	raw = strings.Replace(raw, suffix, "", 1)
	raw = strings.Replace(raw, " ", "", -1)
	raw = strings.Replace(raw, LineBreak, "", -1)
	raw = strings.Replace(raw, "\r", "", -1)
	raw = strings.Replace(raw, "\t", "", -1)

	var sl = len(raw)
	var c = sl / lineCount
	if sl%lineCount > 0 {
		c = c + 1
	}
	var buf bytes.Buffer
	buf.WriteString(prefix + LineBreak)
	for i := 0; i < c; i++ {
		var b = i * lineCount
		var e = b + lineCount
		if e > sl {
			buf.WriteString(raw[b:])
		} else {
			buf.WriteString(raw[b:e])
		}
		buf.WriteString(LineBreak)
	}
	buf.WriteString(suffix)
	return buf.Bytes()
}

func main() {
	// content   := strings.Repeat("H", 244)+"e"
	// content   := strings.Repeat("H", 245)+"e"
	content := `appId=1132440345279104&charSet=UTF-8&mchId=1132439498030720&mchNo=LX1686045288095870000&nonce=jlywbmeb6z&payFee=1&payStatus=1&payTime=2023-06-06 17:55:53&serialsNo=2023060622001422851420568090&signType=RSA2&tradeNo=1136264560671616&version=1.0`
	prvKey := `MIIEuwIBADALBgkqhkiG9w0BAQEEggSnMIIEowIBAAKCAQEAq2FqPV4IzgxdnhoF3QdYSxxy8oLNMd02rg04Ye9uLdfJu//898u6nruD9u7zXy6lbdRoP7XdXq1HIGlQGX+TWGYzj/39ul4ZPdROxRKhsp/OaLiMhyWDaZ00W6UyRddWKHEI+XL3GfzFOoWFkT99tgC0mHoxGX8XiRJ3X86IZdDvyTgbmder+GB7D2k4ErFPzMIvDi22OGBWcW+r1LmrMWuHF1MIc4g6oEoXMSDZUVbzsNeyUVSgl0B5icGwUTtKpOmGCnh2AjyCIF5euzG5jZE26vGP7Taey7vAAPgtNJq1SQbeQJTdh63LlGWlNPfZ5VNkI9+MwLIlg9fQvK/BOQIDAQABAoIBAQCHpsnS3TWW8o6/b9WoSAIJIfGSJxqIF5MKXYh9bGkHfEA/wLXY5bdHoSEpOaYFdwSWVIRXuXoJUJp1+yXdqO9WDz9NADvvYkAUgpH+x2qZ2ogkt77z0iucU0R4LeAHDBU0WZRC7k7MkRkD41//wgOdJh3MexuWFNTqOGWove+UtmqECxsqQ4VcGjiQzJu6mqYMvVQuHIpknj0gl+c6zNU49rHWObyxXzOF4vGBw2BhJPEqUyON4eJuRb49WiU9THug73sjCYe1t5vvrvXl+rkW6+ribFMTkSWPigumN7JEV3uK2IKXEOHdB6Vcy6ihXggdEoO67f5UNrbHy4f4TSLJAoGBAMUr2SvJDg9EwaPLdpwOVIStPp//Pjy2fnMdYlz1pMk2t6IW6lOFeqNxHjOOxYfnYO2L3dZ1gHjPIz9QX5Zm2PLxsTv1PsGKkwzniIIOetJdBwd+wFd7HtDLjoQEamkz5dcgDR26ySbrmV7H/wZgGMmr8+LkD5VhM7FSn5/ExbZDAoGBAN6DpDV2PvqeAdCMeh917PZbZhAA8i0yNUMBMpGHx86nm2SgfmgQ8b4gTghjCS24nnSI8EaMkyjROS/TWDLZoErcqy+/O+wKJaKMgmmiZa1QMrAi80xTFIMsHD34GIG+qsf6+xZYUSv8KE+t92W/czSwRlKG7/0MG0E4gIe6JtjTAoGAGDgEmv49Pd7iMi5hyVVxSELHeHuvt2FrMtSfKm/558VS1RQfgFba84yHeynEVac0HrmZbChOuYgn+jTzKNRFPcI2VPkQ1lEhMuqVt/PzXjeTD3agRZ6X8GmwfcLVF0sKplwHgGlbH+68jgne53eSU+NNN8dvqpef894EQWm4J2UCgYBcoerzgrV3Od5Bhqm0fTBX4vbbRLmNDTDVIyN9KEyLAIWVX6cgBaXN4774iNoiWZBFrVhx1kXRIUCwY0h9atHrOHBfoTn96r9+KwaDmWLAwvlHEFW++Xs5nFxpg+YX5VtNg3OR+tRX/lJ90UuD5S69yYCNDLXN34NdJHuFhX50lwKBgGqdUCKfM1oiD1Cnnr7YtIIp6c3PmPn0cdK+Wb7rTp/u8gU90LSDRgA8GhTcrV6dj/0mmcvDiiAI+FPnWrDNQr972IkhMbQi651P+TyDIb8uehCjKI58SqvT0u8wqyHqKn6lEW6TZ8qnyO9QFBlJHA3pcMR9qOxIQay/heVbOVTz`
	pubKey := `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq2FqPV4IzgxdnhoF3QdYSxxy8oLNMd02rg04Ye9uLdfJu//898u6nruD9u7zXy6lbdRoP7XdXq1HIGlQGX+TWGYzj/39ul4ZPdROxRKhsp/OaLiMhyWDaZ00W6UyRddWKHEI+XL3GfzFOoWFkT99tgC0mHoxGX8XiRJ3X86IZdDvyTgbmder+GB7D2k4ErFPzMIvDi22OGBWcW+r1LmrMWuHF1MIc4g6oEoXMSDZUVbzsNeyUVSgl0B5icGwUTtKpOmGCnh2AjyCIF5euzG5jZE26vGP7Taey7vAAPgtNJq1SQbeQJTdh63LlGWlNPfZ5VNkI9+MwLIlg9fQvK/BOQIDAQAB`

	//自己生成密钥对
	//privateKey, publicKey := NewRsa("", "").CreateKeys(1024)
	//privateKey, publicKey := NewRsa(pubKey, prvKey).CreatePkcs8Keys(2048)
	//fmt.Printf("公钥：%v\n私钥：%v\n", publicKey, privateKey)

	rsaObj := NewRsa(pubKey, prvKey)
	secretData, err := rsaObj.Encrypt([]byte(content))
	if err != nil {
		fmt.Println(err)
	}
	plainData, err := rsaObj.Decrypt(secretData)
	if err != nil {
		fmt.Print(err)
	}
	//
	data := strings.Repeat(content, 200)
	sign, _ := rsaObj.Sign(data, crypto.SHA1)
	verify := rsaObj.Verify(data, sign, crypto.SHA1)
	fmt.Printf(" 加密：%v\n 解密：%v\n 签名：%v\n 验签结果：%v\n",
		hex.EncodeToString(secretData),
		string(plainData),
		sign,
		verify,
	)

	pubKey = `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzW8QkwKu2wrnzNcl0TP7vwFp4FAVTFKC1/E0Ac+01+cyR4m/6zZJSX5ZnaLWnQUMVZjNwjigrTApgRM71aOL7SGsot4W7Bp63L1n04yI7XqxVbu2l/+rqvw7l49opp5bNiloSZHihtJOyMpuW15J/dIbM31/17Dc8VxUGa2JrAgu7sGjRilZWmx3AM+9hu6ydDSY51fE8TDeKsuzhnD3NgkhSwLStQkJcm2j13HP9T7gKfYTEUgR22T10YohIWFu5ji0ddM+HsxvctnBsniKFE7FabboIB/mGreTn38olwtAr/oAd6EK8nkjx74B0mbd2AnPhkwgMbnsc6voT/iPUwIDAQAB`
	sign = `ljB9UihUktbb4MxAgi4MrY8Z8cSW5uGoY/8RXHkS1DC4KvgsOf5Sk3WiQwqfP1XjKbx2H4txEdQiBWQjF9o80doNLgQ4WHHuvw6WfsdPXwd+cfM5ePKPm+D26wOCdUc73BtozW/xDYg6h5wTc6PDmw+G03uLDD9m3h2RzOF++ON/agmyr/s0rrG/Z4UNLRbHHq+hUxhn2OXc/YKXOP85az6AdVoow7S0h7Tl85oyhh3y1cjRFTsnqFawJYNByZIqMKMfqvEL5Gy1OADyL/K6m8BWPWSYmFWPxbJCwlU2ygovoa5ApsR97NkirAgUdB1MbRKmI7bPWzzv5Ygxpc9/Lw==`
	rsaObj = NewRsa(pubKey, prvKey)
	b := rsaObj.Verify(content, sign, crypto.SHA256)
	fmt.Println(b)
}
