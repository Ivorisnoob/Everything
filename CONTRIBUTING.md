# Contributing

First off, thanks for taking the time to contribute.

This is a personal project built for my own needs, but if you find it useful and want to contribute, here's how.

## Ground Rules

- This project is opinionated. It's built the way I like things.
- Not every feature request will be accepted. If it doesn't fit my use case, I probably won't add it.
- That said, feel free to fork and build whatever you want.

## How to Contribute

### Reporting Bugs

If you find a bug:

1. Check if it's already reported in Issues
2. If not, open a new issue with:
   - What you expected to happen
   - What actually happened
   - Steps to reproduce
   - Device/Android version

### Suggesting Features

Before suggesting a feature, consider:

- Does it fit the project's scope?
- Would I actually use it?

If yes to both, open an issue describing:

- The problem you're trying to solve
- Your proposed solution
- Any alternatives you've considered

### Pull Requests

1. Fork the repo
2. Create a branch (`git checkout -b feature/your-feature`)
3. Make your changes
4. Test on a real device
5. Commit with a clear message
6. Push to your fork
7. Open a PR

### Code Style

- Follow existing code patterns
- Use Material 3 Expressive components where applicable
- Keep it simple

## Development Setup

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/everything.git
cd everything

# Build
./gradlew assembleDebug

# Run tests
./gradlew test
```

## Questions?

Open an issue. I'll get to it when I can.
