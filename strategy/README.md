# Strategy Design pattern
**Strategy** design pattern is one of the behavioral design pattern. Strategy pattern is used when we have multiple algorithm for a specific task and client decides the actual implementation to be used at runtime.

Core Components:

**Interface**: Defines the common operation that all concrete strategies must implement. This ensures interchangeability and loose coupling.

**Concrete Strategies**: Implement the interface's method, each encapsulating a different algorithm or behavior.

**Context**: Holds a reference to a concrete strategy object. It uses the strategy's interface to invoke its operation without knowing its specific implementation.
