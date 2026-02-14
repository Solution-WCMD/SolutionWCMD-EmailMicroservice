# SolutionWCMD Email Microservice

The SolutionWCMD Email Microservice is a lightweight backend component responsible for receiving contact and application requests from our platform and forwarding them securely via SMTP. It is part of the SolutionWCMD ecosystem and works together with the frontend repository available at:

[https://github.com/Solution-WCMD/SolutionWCMD-Frontend](https://github.com/Solution-WCMD/SolutionWCMD-Frontend)

The service is intentionally minimal. It exposes a single REST endpoint that accepts validated JSON input and sends the message to a configured target email address. It runs inside our Kubernetes infrastructure and is designed to be stateless, containerized, and horizontally scalable.

---

## What it does

The service provides a POST endpoint at `/contact/send`.
It validates incoming requests and forwards them as formatted emails.

The request body contains a title, name, email and message. Basic validation ensures reasonable input length and required fields before anything is sent.

In production, the service runs inside Kubernetes and is deployed as a Docker image published to GitHub Container Registry.

---

## Configuration

The application is configured entirely through environment variables:

`MAIL_USERNAME`
`MAIL_PASSWORD`
`MAIL_CONTACT`

If any required variable is missing, the service will not start.

## Running locally

Build the project with Gradle and start the generated jar while providing the required environment variables. Once started, the service listens for contact requests and forwards them through the configured SMTP account.

## Deployment

Docker images are automatically built and published when a version tag is pushed. The resulting image is deployed into our Kubernetes cluster, where secrets are injected securely via environment configuration.

## Contributing

Contributions are welcome. If you would like to improve the service, fix issues, or extend functionality, please fork the repository and open a pull request with a clear description of your changes.

We aim to keep this service small, focused, and easy to understand. Changes that increase complexity without clear benefit should be discussed in an issue first.

