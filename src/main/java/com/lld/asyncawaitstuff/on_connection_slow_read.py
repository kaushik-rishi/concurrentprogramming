import asyncio

async def slow_client():
    print("Connecting to server...")
    reader, writer = await asyncio.open_connection('127.0.0.1', 7000)

    total_read = 0
    try:
        while True:
            # Read small chunks (10kb) at a time
            chunk = await reader.read(1024 * 10)
            if not chunk:
                break

            total_read += len(chunk)
            print(f"Read {total_read/1024/1024:.2f}MB so far...")

            # Simulate slow processing
            await asyncio.sleep(0.1)  # Sleep 100ms between reads

    finally:
        print(f"Total read: {total_read/1024/1024:.2f}MB")
        writer.close()
        await writer.wait_closed()

if __name__ == "__main__":
    asyncio.run(slow_client())
