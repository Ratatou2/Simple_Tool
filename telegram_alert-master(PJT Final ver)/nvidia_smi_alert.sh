#!/bin/bash

# 텔레그램 봇 토큰과 채팅 ID 설정
TOKEN="{토큰 입력하기}"
CHAT_ID="{Chat ID 입력하기}"

# 메시지를 4096자 단위로 분할하여 전송
# while [[ ${#output} -gt 0 ]]; do
#     # 처음 4096자를 잘라내어 전송
#     curl -s -X POST https://api.telegram.org/bot$TOKEN/sendMessage -d chat_id=$CHAT_ID -d text="${output:0:3072}"
#     # 나머지 문자열을 업데이트
#     output=${output:3072}
# done
# curl -s -X POST https://api.telegram.org/bot$TOKEN/sendMessage -d chat_id=$CHAT_ID -d text="${output:0:3072}"


# NVIDIA SMI의 결과를 파일로 저장 및 전송
nvidia-smi > nvidia_output.txt
curl -F chat_id=$CHAT_ID -F document=@nvidia_output.txt https://api.telegram.org/bot$TOKEN/sendDocument