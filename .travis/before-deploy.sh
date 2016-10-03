#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_f10b64b8b68a_key -iv $encrypted_f10b64b8b68a_iv -in .travis/codesigning.asc.enc -out codesigning.asc -d
    gpg --fast-import codesigning.asc
fi
