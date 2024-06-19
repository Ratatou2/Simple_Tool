#!/bin/bash

declare -a PID_LIST
declare -a PROCESS_NAMES

echo "Enter the PIDs and their names to monitor (e.g., PID1,Name1;PID2,Name2; ...): "
read -a PID_NAMES_ARRAY

# 프로세스 종료시 텔레그램 msg 발송
send_termination_message() {
    local message="[System][$(date '+%Y.%m.%d %I:%M %p')][${1}][${2}] 가 종료되었습니다."
    python /home/j-k10s106/telegram_alert-master/telegram_alert.py "${message}"
    echo "[System][$(date '+%Y.%m.%d %I:%M %p')][${1}][${2}] 가 종료되었습니다."
}

# 모든 프로세스 종료시 텔레그램 msg 발송
send_all_terminated_message() {
    python /home/j-k10s106/telegram_alert-master/telegram_alert.py "=== 모든 프로세스 종료 ==="
}

python /home/j-k10s106/telegram_alert-master/telegram_alert.py "=== 프로세스 탐지기 시작 ==="



# ';'로 구분된 PID와 이름 추출
IFS=';' read -ra PID_NAMES_ARRAY <<< "${PID_NAMES_ARRAY}"

# PID와 이름 추출
for entry in "${PID_NAMES_ARRAY[@]}"; do
    IFS=',' read -r -a pid_name <<< "$entry"
    PID_LIST+=("${pid_name[0]}")
    PROCESS_NAMES+=("${pid_name[1]}")
done

# 텔레그램 알림 템플릿 함수
send_telegram_message() {
    local message="[System] 텔레그램 알림이 실행되었습니다"
    message+=$'\n'"실행시간 : $(date '+%Y.%m.%d %I:%M %p')"  # 현재 시간
    for ((i = 0; i < ${#PID_LIST[@]}; i++)); do
        message+=$'\n'"[${PID_LIST[i]}] : ${PROCESS_NAMES[i]}"
    done
    python /home/j-k10s106/telegram_alert-master/telegram_alert.py "${message}"
    echo "${message}"
}

# 텔레그램 알림 시작 메시지
send_telegram_message

# 병렬적으로 각각의 while문 돌려서 프로세스 종료를 체크
for pid_name in "${PID_NAMES_ARRAY[@]}"; do
    IFS=',' read -r -a pid_name_arr <<< "$pid_name"
    PID=${pid_name_arr[0]}
    NAME=${pid_name_arr[1]}
    echo "Monitoring PID ${PID} (${NAME}) in the background..."
    (while ps -p $PID > /dev/null; do sleep 60; done; send_termination_message "${PID}" "${NAME}") &
done

wait

# 모든 프로세스 종료시 메세지 보내고 터미널에 echo
send_all_terminated_message
echo "[System][$(date '+%Y.%m.%d %I:%M %p')] All monitored processes have terminated. Script exiting."
