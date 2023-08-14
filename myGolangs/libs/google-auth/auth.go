package main

import (
	"fmt"

	googleidtokenverifier "github.com/movsb/google-idtoken-verifier"
)

func main() {
	//Iss:	https://accounts.google.com
	//Sub:	112107205971195397783
	//Email:	tommyjarvis657@gmail.com
	//Name:	哈哈卡卡
	//Domain:
	//token := "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3NmRhOWQzMTJjMzlhNDI5OTMyZjU0M2U2YzFiNmU2NTEyZTQ5ODMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2ODk2NzI0ODgsImF1ZCI6Ijk5MTk3NjY3OTM5My1ibzAyMG5oODN0MG8wcnZqcWpkY3J1Ymhsa2xlY2EwYi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExMjEwNzIwNTk3MTE5NTM5Nzc4MyIsImVtYWlsIjoidG9tbXlqYXJ2aXM2NTdAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF6cCI6Ijk5MTk3NjY3OTM5My1ibzAyMG5oODN0MG8wcnZqcWpkY3J1Ymhsa2xlY2EwYi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsIm5hbWUiOiLlk4jlk4jljaHljaEiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUFjSFR0Y1l3Y1p4S3l3NVpxOGNFR0Fac0dXdjFnZjA5Z1V2S1RmbmpBV2MzZjBKPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuWNoeWNoSIsImZhbWlseV9uYW1lIjoi5ZOI5ZOIIiwiaWF0IjoxNjg5NjcyNzg4LCJleHAiOjE2ODk2NzYzODgsImp0aSI6IjI3N2Y1Mzc3YjY3YTgzNTc3NmU3MmU0MjRlNzUyOTE4NTg0NTNmZWMifQ.NcU6qdJZXCAo29qo-1_AiDfG5rejeGLT9Z1YOoLgHITK7JwFaLNu02coGEmLaC5N9ZNwnnLFvKdk1wHRC04Nr7NVtstVNt50eu1d3Sl9a6LooEFLiG6D-P01ctMiqkbneV9c5nmMzUSHxsP_TGR1s6EI6n2TU-_0ZY8cMBKUwm8ZNF2F0_SZa6azCW6m-lB9FGMJ3MTdmlFI0RfAPQlhdV0rScPVNycPGNJ6q6bASJNjKpBRHomIfkhay4cnefuFcx2Cr9zSgZ9R9-u5QPG4hrkHh6jc_MejleSgiAb0RUEs094Yi4zhHCV9dsaRWOJaf2Kg_bGl7jM_R-d0-mEwoQ"
	token := "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3NmRhOWQzMTJjMzlhNDI5OTMyZjU0M2U2YzFiNmU2NTEyZTQ5ODMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2ODk2NzQxODYsImF1ZCI6Ijk5MTk3NjY3OTM5My1ibzAyMG5oODN0MG8wcnZqcWpkY3J1Ymhsa2xlY2EwYi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExNzk3MjAxMjgxOTk5Mjg3NDI0NiIsImVtYWlsIjoiamFydmlzdG9tbXk2NTc2QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI5OTE5NzY2NzkzOTMtYm8wMjBuaDgzdDBvMHJ2anFqZGNydWJobGtsZWNhMGIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJuYW1lIjoidG9tbXkgamFydmlzIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FBY0hUdGZidEp0d053M3RXdWVTdFdlUms4VV9aWUtaMGU4cmFCNDVJTjE3SnRVPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6InRvbW15IiwiZmFtaWx5X25hbWUiOiJqYXJ2aXMiLCJpYXQiOjE2ODk2NzQ0ODYsImV4cCI6MTY4OTY3ODA4NiwianRpIjoiNjBjZmQwMDA5N2E1MTE4YjdhN2ZmNGJhZTUwNDdmNTJjYTk5MGU5ZCJ9.jf6wxhjVHb1FhdbthAgGT70cOur5GU2EAIHWC731mnOyqTQeu-n3qHKuJpGYNHcnA-WtMZC8xBd7dfvBUxRJPhpRsWzmkjn5lzGExBjxKjRlhWLT9OFtkDlJD-6Hk_M16wJqMtrIGAWzXaECHIuHnRIGlbq9578OUZLzuq2pRkdq7bN1K8UlRYcMamX5oLdLsL5oXp46fTJOyjjsU8sdDG1m2FWr8wCoYKgYZw5S6bV61PLmEJAQ0-Fkcdj2LHF36tX2rEx1NqQdllQ9UgwQGNF_a4ZVL5zocFRbg0SVcrCUFdsqy5Jz3OgPIV-xcEDfftzzasem2UL2EVzKXkiODg"
	clientID := "991976679393-bo020nh83t0o0rvjqjdcrubhlkleca0b.apps.googleusercontent.com"
	claims, err := googleidtokenverifier.Verify(token, clientID)
	if err != nil {
		fmt.Println(err)
		panic(err)
	}
	fmt.Printf("Iss:\t%s\nSub:\t%s\nEmail:\t%s\nName:\t%s\nDomain:\t%s\n",
		claims.Iss, claims.Sub, claims.Email, claims.Name, claims.Domain)
}
