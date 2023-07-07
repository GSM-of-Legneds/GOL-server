# GPG 암호화 사용법
## 암호화
1. 명령어 실행
```
gpg -c <파일명>
```
2. `passphrase` 입력
복호화 할 때 사용합니다. 이후 다시 확인할 수 없으니 조심해서 관리하세요.

## 복호화
```bash
gpg -d -o {복호화 한 파일 생성할 경로} \ 
--pinentry-mode=loopback \
--passphrase {패스워드} \
{암호화된 파일 경로}
```

## 사용 예시
#### 암호화
```bash
gpg -c ./api/src/main/resources/application-local.yml
```
```bash
passphrase: passward
```

#### 복호화
```bash
gpg -d -o ./api/src/main/resources/application-local.yml \
--pinentry-mode=loopback \
--passphrase asd \
--yes \
./api/src/main/resources/application-local.yml.gpg
```
기존 파일을 overwrite 하도록 물어봤을 때 허용하도록 `--yes` 명령어 추가 

## 사용 시 주의점

암호화 시 passphrase가 8글자 이하이거나
소문자, 대문자, 특수문자 중 1종류 이하라면 해당 번호를 확인하는게 맞는지 확인합나디.

복호화 시 복호화 할 경로에 파일이 이미 존재한다면 overwrite 할지 확인합니다.

스크립트로 작성 시 주의하세요.
