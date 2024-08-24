import argparse as ap
import dotenv as de
import os
import typing as t


def _parse_args() -> t.Tuple[str, str]:
    parser = ap.ArgumentParser()
    parser.add_argument('-f', '--filename', default='.env', help="The environment filename.")
    parser.add_argument('-k', '--key', required=True, help="The key of target value.")
    args: ap.Namespace = parser.parse_args()
    # print(f'Parsed scripts arguments: filename={args.filename}, key={args.key}')
    return args.filename, args.key


def load_env_file(env_file: str):
    if os.path.isfile(env_file):
        de.load_dotenv(env_file)
    else:
        raise FileNotFoundError(f"The file {env_file} does not exist.")


def get_env_variable(key: str) -> str:
    return os.getenv(key)


def main():
    _filename, _key = _parse_args()
    env_file = load_env_file(_filename)
    print(get_env_variable(_key))


# --------------------------------------------------------------------------------------------------
# script executing below
main()
