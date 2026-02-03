<script setup lang="ts">
import { useSignUpForm } from './useSignUpForm';

const { name, email, password, confirmPassword, isLoading, errorMessage, handleSignUp } = useSignUpForm();
</script>

<template>
  <div class="signup-container">
    <div class="signup-card">
      <div class="header">
        <div class="finance-icon-wrapper">
          <img src="/public/assets/finance-icon.svg"></img>
        </div>
        <h1>FinanceApp</h1>
        <p>Take control of your finances</p>
      </div>
      <div class="signup-form">
        <h4>Welcome</h4>
        <h5>Sign in to your account or create a new one</h5>
        <form @submit.prevent="handleSignUp">
          <div class="form-group">
            <label for="name">Full Name</label>
            <input
              id="name"
              v-model="name"
              type="text"
              placeholder="e.g. Jane Doe"
              required
            />
          </div>
          <div class="form-group">
            <label for="email">Email Address</label>
            <input
              id="email"
              v-model="email"
              type="email"
              placeholder="you@example.com"
              required
            />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input
              id="password"
              v-model="password"
              type="password"
              placeholder="Minimum 8 characters"
              required
            />
          </div>

          <div class="form-group">
            <div class="label">
              <label for="confirmPassword">Confirm Password</label>
            </div>
            <input
              id="confirmPassword"
              v-model="confirmPassword"
              type="password"
              placeholder="Confirm your password"
              required
            />
          </div>

          <button type="submit" class="signup-button" :disabled="isLoading">
            <span v-if="isLoading">Creating Account...</span>
            <span v-else>Create Account</span>
          </button>

          <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </div>

        </form>
        <div class="form-footer">
          <p>
            Already have an account? <RouterLink to="/login">Sign In</RouterLink>
          </p>
        </div>
      </div>
      <div class="bottom">
        <div class="feature-item">
          <div class="icon-wrapper">
            <img src="/public/assets/shield-icon.svg"></img>
          </div>
          <div class="text-wrapper">
            <h5>Bank-level Security</h5>
            <h6>Your data is protected with 256-bit encryption</h6>
          </div>
        </div>
        <div class="feature-item">
          <div class="icon-wrapper">
            <img src="/public/assets/lightning-bolt-icon.svg"></img>
          </div>
          <div class="text-wrapper">
            <h5>Instant Transfers</h5>
            <h6>Send and receive money in real-time</h6>
          </div>
        </div>
        <div class="feature-item">
          <div class="icon-wrapper">
            <img src="/public/assets/bar-chart-icon.svg"></img>
          </div>
          <div class="text-wrapper">
            <h5>Smart Analytics</h5>
            <h6>Track your spending with detailed insights</h6>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped >
.signup-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: radial-gradient(circle at top, #f0f2f5, #e3eaf6);
  font-family: sans-serif;
}

.signup-card {
  padding: 2.5rem;
  border-radius: 8px;
  width: 100%;
  max-width: 420px;
  text-align: center;
}

.header {
  text-align: center;
}

.signup-form {
  justify-content: center;
  align-items: center;
  background: white;
  padding: 1.5rem;
  border-radius: 20px;
  max-width: 420px;
  text-align: center;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
  font-size: 0.8rem;
  border: 1px solid #e5e7eb;
}

.finance-icon-wrapper {
  background-color: #bbd1ed;
  border-radius: 16px;
  padding: 1rem;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  color: #4285F4;
}

.finance-icon-wrapper img {
  width: 40px;
  height: 40px;
}

.header h1 {
  font-size: 2rem;
  margin-bottom: 0.1rem;
  color: #333;
}

.header p {
  font-weight: 400;
  color: #848484;
  font-size: 1rem;
  margin-top: 0.3rem;
  margin-bottom: 2rem;
}

.signup-form h4 {
  font-weight: 400;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  color: #474646;
}

.signup-form h5 {
  font-weight: 400;
  font-family:Verdana, Geneva, Tahoma, sans-serif;
  color: #8e8b8b;
  font-size: 0.7rem;
  margin-top: 0.1rem;
  margin-bottom: 1.5rem;
}


.form-group {
  margin-bottom: 1.0rem;
}

.form-group label{
  text-align: left;
  margin-left: 20px;
}

.form-group input{
  background-color: #f0f2f5;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 400;
  color: #555;
}

input {
  width: 90%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-sizing: border-box;
  font-size: 0.8rem;
  color: black;
}

.signup-button {
  width: 90%;
  padding: 0.9rem;
  background-color: #383a38;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 600;
  transition: background-color 0.2s;
}

.signup-button:hover {
  background-color: #4a4d4a;
}

.form-footer {
  margin-top: 1.5rem;
  font-size: 0.8rem;
  color: #474646;
}

.form-footer a {
  color: #007bff;
  text-decoration: none;
  font-weight: 600;
}

.form-footer a:hover {
  text-decoration: underline;
}

.bottom {
  display: flex;
  flex-direction: column;
  gap: 1.0rem;
  font-family: sans-serif;
  padding: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  flex-direction: row;
  gap: 1.2rem;
}

.text-wrapper h5{
  color: #474646;
  margin-top: 0.1rem;
  margin-bottom: 0.1rem;
  display:flex;
  align-items: center;
  padding: 0.1rem;
}

.text-wrapper h6{
  font-family:Verdana, Geneva, Tahoma, sans-serif;
  color: #afacac;
  margin-top: 0.1rem;
  margin-bottom: 0.1rem;
  display:flex;
  align-items: center;
  padding: 0.1rem;
  white-space: nowrap;
}

.icon-wrapper {
  background-color: #e3eaf6;
  border-radius: 12px;
  padding: 0.7rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #3b82f6;
}

.icon-wrapper img {
  width: 20px;
  height: 20px;
}

.error-message{
  margin-top: 0.4rem;
  color: rgb(181, 35, 35);
}

</style>
