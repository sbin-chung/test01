import { test } from "node:test";
import assert from "node:assert/strict";
import { add, subtract, multiply, divide, isPrime } from "../src/math.js";

test("add", () => {
  assert.equal(add(2, 3), 5);
});

test("subtract", () => {
  assert.equal(subtract(5, 2), 3);
});

test("multiply", () => {
  assert.equal(multiply(4, 3), 12);
});

test("divide", () => {
  assert.equal(divide(10, 2), 5);
});

test("divide by zero throws", () => {
  assert.throws(() => divide(1, 0), /Division by zero/);
});

test("isPrime", () => {
  assert.equal(isPrime(7), true);
  assert.equal(isPrime(8), false);
  assert.equal(isPrime(1), false);
});
