import argparse as ap
import dotenv as de
import typing as t
import os
import sys


def _parse_args() -> str:
    parser = ap.ArgumentParser()
    parser.add_argument('-e', '--envfilename', default='./.env', help="The *.env filename.")
    args: ap.Namespace = parser.parse_args()
    print(f'Parsed script argument: env-filename={args.envfilename}')
    return args.envfilename

def load_env_file(env_file: str) -> t.Dict[str, str]:
    if os.path.isfile(env_file):
        return de.dotenv_values(env_file)
    else:
        raise FileNotFoundError(f"The file {env_file} does not exist.")

def calculate_memory_limits() -> t.Tuple[int, int, int, int]:
    # Узнаем общий объем оперативной памяти в мегабайтах
    total_mem = os.popen("free -m | awk '/^Mem:/{print $2}'").read().strip()
    total_mem = int(total_mem)

    # Вычисляем размеры для приложений
    postgres_mem = total_mem * 15 // 100        # вынести в аргументы процент
    spring_boot_mem = total_mem * 35 // 100     # вынести в аргументы процент
    angular_mem = total_mem * 20 // 100         # вынести в аргументы процент

    return total_mem, postgres_mem, spring_boot_mem, angular_mem

def update_or_add_env_variable(env_file: str, key: str, value: str):
    # Получаем все переменные окружения из файла
    variables = de.dotenv_values(env_file)

    # Проверяем, существует ли ключ в текущих переменных
    variables[key] = value  # обновление или добавление переменной

    # Записываем обновленные переменные обратно в файл
    with open(env_file, 'w') as file:
        for k, v in variables.items():
            file.write(f"{k}={v}\n")

def main():
    _env_filename = _parse_args()
    variables = load_env_file(_env_filename)  # Загружаем переменные окружения

    # Вычисляем размеры памяти
    total_mem, postgres_mem, spring_boot_mem, angular_mem = calculate_memory_limits()

    # Обновляем или добавляем переменные в .env файл
    update_or_add_env_variable(_env_filename, 'TOTAL_MEM', str(total_mem))
    update_or_add_env_variable(_env_filename, 'POSTGRES_MEM', str(postgres_mem))
    update_or_add_env_variable(_env_filename, 'SPRING_BOOT_MEM', str(spring_boot_mem))
    update_or_add_env_variable(_env_filename, 'ANGULAR_MEM', str(angular_mem))

    print(f"Updated environment variables in {_env_filename}.")

if __name__ == "__main__":
    main()