import asyncio
import websockets
import json
import threading
import time

# Import the Java helper class
from java import jclass

# Get a reference to the Java class
ActionHelper = jclass('com.example.aiagent.ActionHelper')

async def handler(websocket):
    async for message in websocket:
        data = json.loads(message)
        command = data.get("command")
        params = data.get("params", {})

        if command == "notify":
            title = params.get("title", "AI Agent")
            text = params.get("text", "Hello from Python!")
            ActionHelper.sendNotification(title, text)

        elif command == "vibrate":
            duration = params.get("duration", 500)  # milliseconds
            ActionHelper.vibrate(duration)

        elif command == "toast":
            msg = params.get("message", "Hello")
            ActionHelper.showToast(msg)

        else:
            print(f"Unknown command: {command}")

async def main():
    async with websockets.serve(handler, "127.0.0.1", 9002):
        print("WebSocket server started on 127.0.0.1:9002")
        await asyncio.Future()

def main():
    thread = threading.Thread(target=start_server, daemon=True)
    thread.start()
    while True:
        time.sleep(1)

def start_server():
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(main())
