import telepot
import sys

# 텔레그램 봇의 API Token을 여기에 입력
TELEGRAM_BOT_TOKEN = '{봇 토큰}'

# 받는 사람의 Chat ID를 여기에 입력
CHAT_ID = '{CHAT ID 입력}'

def send_telegram_message(message):
    bot = telepot.Bot(TELEGRAM_BOT_TOKEN)
    bot.sendMessage(CHAT_ID, message)

# if len(sys.argv) <= 1:
#     print("# need more parameter")
#     sys.exit(1)

# 명령행 인자로 전달된 메시지를 가져옴
message_content = ' '.join(sys.argv[1:])

# 메시지 전송
send_telegram_message(message_content)
